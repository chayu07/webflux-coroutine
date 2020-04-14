package com.victor.example.webfluxcoroutine.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toMono

/**
 *
 */
@Configuration
class HealthRouter {

    @Bean(name = ["healthRouterFunction"])
    fun routerFunction() = router {

        ("/api" and accept(APPLICATION_JSON)).nest {
            GET("/ping") {
                "pong".toMono()
                        .flatMap {
                            val notFound = ServerResponse.notFound().build()
                            ServerResponse.ok()
                                    .body(fromObject(it))
                                    .switchIfEmpty(notFound)
                        }.subscribeOn(Schedulers.elastic())
            }
        }
    }
}