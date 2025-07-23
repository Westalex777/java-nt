package main

import (
	"fmt"
	"io"
	"net/http"
	"os"
	"strconv"
	"strings"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promhttp"
	"github.com/shirou/gopsutil/cpu"
	"github.com/shirou/gopsutil/load"
	"github.com/shirou/gopsutil/process"
)

var (
	reqCount = prometheus.NewCounterVec(
		prometheus.CounterOpts{
			Name: "http_server_requests_seconds_count",
			Help: "Number of HTTP requests by status and method",
		},
		[]string{"method", "uri", "status"},
	)
	reqDuration = prometheus.NewHistogramVec(
		prometheus.HistogramOpts{
			Name:    "http_server_requests_seconds_sum",
			Help:    "Total time spent handling requests",
			Buckets: prometheus.DefBuckets,
		},
		[]string{"method", "uri", "status"},
	)
	activeConnections = prometheus.NewGauge(
		prometheus.GaugeOpts{
			Name: "http_server_requests_active_seconds",
			Help: "Number of currently active SSE connections",
		},
	)
	processCPU = prometheus.NewGauge(prometheus.GaugeOpts{
		Name: "process_cpu_usage",
		Help: "Process CPU usage (0..1)",
	})
	systemCPU = prometheus.NewGauge(prometheus.GaugeOpts{
		Name: "system_cpu_usage",
		Help: "System CPU usage (0..1)",
	})
	systemLoad1m = prometheus.NewGauge(prometheus.GaugeOpts{
		Name: "system_load_average_1m",
		Help: "System load average (1m)",
	})
	heapUsed = prometheus.NewGaugeVec(prometheus.GaugeOpts{
		Name: "jvm_memory_used_bytes",
		Help: "Heap memory used (mapped for Micrometer dashboard)",
	}, []string{"area"})
	liveThreads = prometheus.NewGauge(prometheus.GaugeOpts{
		Name: "jvm_threads_live_threads",
		Help: "Number of live threads (goroutines mapped)",
	})
	proc *process.Process
)

func init() {
	prometheus.MustRegister(reqCount, reqDuration, activeConnections)
	prometheus.MustRegister(processCPU, systemCPU, systemLoad1m, heapUsed, liveThreads)

	p, _ := process.NewProcess(int32(os.Getpid()))
	proc = p

	go func() {
		for {
			updateSystemMetrics()
			time.Sleep(2 * time.Second)
		}
	}()
}

func updateSystemMetrics() {
	if proc != nil {
		if pcpu, err := proc.CPUPercent(); err == nil {
			processCPU.Set(pcpu / 100.0)
		}
		if memInfo, err := proc.MemoryInfo(); err == nil {
			heapUsed.WithLabelValues("heap").Set(float64(memInfo.RSS))
		}
		if threads, err := proc.NumThreads(); err == nil {
			liveThreads.Set(float64(threads))
		}
	}
	if scpu, err := cpu.Percent(0, false); err == nil && len(scpu) > 0 {
		systemCPU.Set(scpu[0] / 100.0)
	}
	if avg, err := load.Avg(); err == nil {
		systemLoad1m.Set(avg.Load1)
	}
}

func proxyStream(w http.ResponseWriter, r *http.Request) {
	start := time.Now()
	activeConnections.Inc()
	defer activeConnections.Dec()

	length, _ := strconv.Atoi(r.URL.Query().Get("length"))
	if length <= 0 {
		length = 5
	}
	latency, _ := strconv.Atoi(r.URL.Query().Get("latency"))
	if latency < 0 {
		latency = 500
	}
	timeout, _ := strconv.ParseInt(r.URL.Query().Get("timeout"), 10, 64)
	if timeout <= 0 {
		timeout = 30000
	}

	baseURL := fmt.Sprintf("http://mock:8080/stream")
	query := r.URL.Query()
	targetURL := baseURL

	if len(query) > 0 {
		targetURL += "?" + query.Encode()
	}

	upstream, err := http.Get(targetURL)
	if err != nil {
		http.Error(w, fmt.Sprintf("Failed to connect upstream: %v", err), http.StatusBadGateway)
		recordMetrics("GET", "/stream", "502", start)
		return
	}
	defer upstream.Body.Close()

	w.Header().Set("Content-Type", "text/event-stream; charset=utf-8")
	w.Header().Set("Cache-Control", "no-cache")
	w.Header().Set("Connection", "keep-alive")

	flusher, ok := w.(http.Flusher)
	if !ok {
		http.Error(w, "Streaming unsupported", http.StatusInternalServerError)
		return
	}

	buf := make([]byte, 1024)
	deadline := time.Now().Add(time.Duration(timeout) * time.Millisecond)
	doneSent := false

	for {
		if time.Now().After(deadline) {
			if !doneSent {
				w.Write([]byte("data: [TIMEOUT]\n\n"))
				flusher.Flush()
			}
			return
		}

		n, err := upstream.Body.Read(buf)
		if n > 0 {
			chunk := buf[:n]
			text := string(chunk)

			if idx := strings.Index(text, "[DONE"); idx != -1 {

				if idx > 0 {
					w.Write([]byte(text[:idx]))
					flusher.Flush()
				}

				if !doneSent {
					w.Write([]byte("[DONE]\n\n"))
					flusher.Flush()
					doneSent = true
				}

				return
			}

			w.Write(chunk)
			flusher.Flush()
		}

		if err != nil {
			if err == io.EOF {
				if !doneSent {
					w.Write([]byte("[DONE]\n\n"))
					flusher.Flush()
					doneSent = true
				}
			}
			return
		}
	}

}

func recordMetrics(method, uri, status string, start time.Time) {
	elapsed := time.Since(start).Seconds()
	reqCount.WithLabelValues(method, uri, status).Inc()
	reqDuration.WithLabelValues(method, uri, status).Observe(elapsed)
}

func main() {
	r := chi.NewRouter()
	r.Get("/stream", proxyStream)
	r.Handle("/actuator/prometheus", promhttp.Handler())

	port := os.Getenv("PORT")
	if port == "" {
		port = "8084"
	}
	fmt.Printf("Starting SSE proxy on :%s\n", port)
	http.ListenAndServe(":"+port, r)
}
