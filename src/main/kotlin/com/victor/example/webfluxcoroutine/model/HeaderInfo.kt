package com.victor.example.webfluxcoroutine.model

import com.victor.example.kotlinx.getHeaderOrEmptyString
import org.springframework.web.reactive.function.server.ServerRequest

data class HeaderInfo(val userAgent: String) {
    companion object {
        private const val HEADER_USER_AGENT = "User-Agent"
        @JvmStatic
        fun of(req: ServerRequest): HeaderInfo =
                HeaderInfo(
                        userAgent = req.getHeaderOrEmptyString(HEADER_USER_AGENT)
                )
    }
}