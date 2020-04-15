package com.victor.example.kotlinx

import com.victor.example.webfluxcoroutine.code.ErrorCd
import com.victor.example.webfluxcoroutine.exception.ApiException
import com.victor.example.webfluxcoroutine.model.HeaderInfo.Companion.HEADER_USER_AGENT
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.net.URI

fun ServerRequest.userAgentWithoutMono(): String {
    val headers = this.headers().header(HEADER_USER_AGENT)
    return if (headers.isEmpty()) {
        throw ApiException(ErrorCd.BAD_REQUEST, "User-agent missing")
    } else {
        val userAgent = headers[0]
        userAgent
    }
}

fun ServerRequest.userAgent(): Mono<String> = this.userAgentWithoutMono().toMono()

fun HttpHeaders.toPrettyFormat(): String {
    val sb = StringBuilder()
    for (entry in this) {
        sb.append("    ${entry.key}: ${entry.value} \n")
    }

    return sb.toString()
}

fun MultiValueMap<String, ResponseCookie>.toPrettyFormat(): String {
    val sb = StringBuilder()
    for (each in this.values) {
        sb.append(each.toString())
    }
    return sb.toString()
}

fun ServerRequest.getHeaderOrEmptyString(headerName: String): String =
        this.headers().header(headerName).getFirstOrEmptyString()

/**
 * 308은 안드로이드 킷켓에서 리다이렉트가 제대로 동작하지 않음. 그래서 307로 변경함. 308로 변경하지 말것!
 */
fun redirect(uri: String): Mono<ServerResponse> = ServerResponse.temporaryRedirect(URI.create(uri)).build()
fun redirectBuilder(uri: String): ServerResponse.BodyBuilder = ServerResponse.temporaryRedirect(URI.create(uri))