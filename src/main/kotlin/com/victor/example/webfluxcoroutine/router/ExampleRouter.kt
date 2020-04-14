package com.victor.example.webfluxcoroutine.router

import com.victor.example.webfluxcoroutine.handler.ExampleHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class ExampleRouter(
        val exampleHandler: ExampleHandler) {

    @Bean(name = ["authRouterFunction"])
    fun route() = router {
        ("/api" and accept(MediaType.APPLICATION_JSON)).nest {
            ("/auth").nest {
                POST("/v1/request-auth", exampleHandler::requestAuth)
                POST("/v1/confirm-auth", exampleHandler::confirmAuth)
            }
        }
    }
}