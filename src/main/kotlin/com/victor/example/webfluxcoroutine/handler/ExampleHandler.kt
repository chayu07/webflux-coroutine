package com.victor.example.webfluxcoroutine.handler

import com.victor.example.webfluxcoroutine.code.ErrorCd
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Service
class ExampleHandler : AbstractHandler() {

    fun requestAuth(req: ServerRequest): Mono<ServerResponse> {
        return handleWithRequestBody(req) { req: String ->
            "".toMono()
        }.onErrorResume { handleException(it, req, ErrorCd.INTERNAL_ERROR) }
    }

    fun confirmAuth(req: ServerRequest): Mono<ServerResponse> {
        return handleWithRequestBody(req) { req: String ->
            "".toMono()
        }.onErrorResume { handleException(it, req, ErrorCd.INTERNAL_ERROR) }
    }
}