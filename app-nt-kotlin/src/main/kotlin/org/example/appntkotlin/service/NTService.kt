package org.example.appntkotlin.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import kotlinx.coroutines.reactor.awaitSingle


@Service
class NTService {

    @Value("\${integration.mock.url}")
    private lateinit var url: String

    private val webClient = WebClient.create()

    suspend fun test(): String = withContext(Dispatchers.IO) {
        coroutineScope {
            val request1 = async {
                webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .awaitSingle()
            }

            val request2 = async {
                webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .awaitSingle()
            }

            val request3 = async {
                webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .awaitSingle()
            }

            val r1 = request1.await()
            val r2 = request2.await()
            val r3 = request3.await()

            "Results:\n$r1\n$r2\n$r3"
        }
    }
}
