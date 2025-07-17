import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8085) {
        module()
    }.start(wait = true)
}

val client = HttpClient(CIO) {
    engine {
        requestTimeout = 5000
    }
    expectSuccess = false
}

fun Application.module() {

    environment.monitor.subscribe(ApplicationStopping) {
        client.close()
    }

    routing {
        get("/proxy") {
            try {
                val result: String = client.get("http://mock:8080/mock").body()
                call.respondText(result)
            } catch (e: Exception) {
                call.respondText("Request failed: ${e.localizedMessage}", status = HttpStatusCode.BadGateway)
            }
        }
    }
}
