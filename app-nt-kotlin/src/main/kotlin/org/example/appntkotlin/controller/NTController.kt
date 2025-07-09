package org.example.appntkotlin.controller

import org.example.appntkotlin.service.NTService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class NTController(

    private val service: NTService

) {
    private val log = LoggerFactory.getLogger(NTController::class.java)

    @GetMapping("/test")
    suspend fun test(): String {
        log.info("Request /test")
        return service.test()
    }
}