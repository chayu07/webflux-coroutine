package com.victor.example.webfluxcoroutine.handler

import com.victor.example.webfluxcoroutine.code.ErrorCd
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Service
class ExampleHandler : AbstractCoHandler() {

    suspend fun requestAuth(req: ServerRequest): ServerResponse {
        return handleWithRequestBody(req) { req: String ->
            ""
        }
    }

    suspend fun confirmAuth(req: ServerRequest): ServerResponse {
        return handleWithRequestBody(req) { req: String ->
            ""
        }
    }
}