package com.victor.example.webfluxcoroutine.model

import com.victor.example.webfluxcoroutine.HEADER_USER_AGENT
import com.victor.example.kotlinx.appUuidWithoutMono
import com.victor.example.kotlinx.getHeaderOrEmptyString
import com.victor.example.kotlinx.remoteIpWithoutMono
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

data class HeaderInfo(val userAgent: String) {
    companion object {
        @JvmStatic
        fun resolve(req: ServerRequest): Mono<HeaderInfo> =
                HeaderInfo(
                        userAgent = req.getHeaderOrEmptyString(HEADER_USER_AGENT),
                ).toMono()

    }
}