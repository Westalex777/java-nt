FROM golang:1.23-alpine AS builder

WORKDIR /app
COPY . .


RUN apk add --no-cache git


RUN go mod tidy && go build -o /goapp ./cmd/main.go


FROM alpine:3.20

WORKDIR /app
COPY --from=builder /goapp /app/goapp

EXPOSE 8084
ENTRYPOINT ["/app/goapp"]
