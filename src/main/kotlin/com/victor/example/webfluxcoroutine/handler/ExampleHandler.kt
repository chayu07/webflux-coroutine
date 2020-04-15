package com.victor.example.webfluxcoroutine.handler

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Service
class ExampleHandler : AbstractCoHandler() {

    suspend fun request(req: ServerRequest): ServerResponse {
        return handleWithRequestBody(req) { req: String ->
            "request"
        }
    }

    suspend fun confirm(req: ServerRequest): ServerResponse {
        return handleWithRequestBody(req) { req: String ->
            "confirm"
        }
    }
}