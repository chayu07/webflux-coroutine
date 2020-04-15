package com.victor.example.webfluxcoroutine.router

import kotlinx.coroutines.coroutineScope
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.router
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toMono

/**
 *
 */
@Configuration
class HealthRouter {

    @Bean(name = ["health"])
    fun routerFunction() = coRouter {

        ("/api" and accept(APPLICATION_JSON)).nest {
            GET("/ping") {
                coroutineScope {
                    ServerResponse.ok().bodyValueAndAwait("pong")
                }
            }
        }
    }
}