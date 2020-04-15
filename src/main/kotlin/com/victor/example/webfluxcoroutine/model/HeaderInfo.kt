package com.victor.example.webfluxcoroutine.model

import com.victor.example.kotlinx.getHeaderOrEmptyString
import com.victor.example.webfluxcoroutine.HEADER_USER_AGENT
import org.springframework.web.reactive.function.server.ServerRequest

data class HeaderInfo(val userAgent: String) {
    companion object {
        @JvmStatic
        fun of(req: ServerRequest): HeaderInfo =
                HeaderInfo(
                        userAgent = req.getHeaderOrEmptyString(HEADER_USER_AGENT)
                )
    }
}