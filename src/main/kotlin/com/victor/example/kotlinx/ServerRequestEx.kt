package com.victor.example.kotlinx

import com.victor.example.webfluxcoroutine.*
import com.victor.example.webfluxcoroutine.code.ErrorCd
import com.victor.example.webfluxcoroutine.exception.ApiException
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.net.URI


fun ServerRequest.appUuidWithoutMono(): String {
    val appUuidFromHeader = this.getHeaderOrEmptyString(HEADER_UUID)
    if (appUuidFromHeader.isNotEmpty()) {
        return appUuidFromHeader
    }

    val xAuthorization = this.getHeaderOrEmptyString(HEADER_X_AUTHORIZATION)
    return if (xAuthorization.isEmpty()) {
        EMPTY_STRING
    } else {
        if (xAuthorization.contains(AccessToken.PAY_AUTH)) {
            val appUuidFromXAuth = xAuthorization.replace(Regex("^[^-]*-"), "")
            appUuidFromXAuth
        } else {
            this.getHeaderOrEmptyString(X_PAY_UUID)
        }
    }
}

fun ServerRequest.appUuid(): Mono<String> {
    val appUuid = this.appUuidWithoutMono()
    return if (appUuid.isEmpty()) {
        ApiException(ErrorCd.BAD_REQUEST, "Authorization missing").toMono()
    } else {
        appUuid.toMono()
    }
}

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

/**
 * kpdevice는 아이폰, 안드로이드에서 기기정보가 올라오는 헤더.
 * 예) ios -> iPhone8, and -> SM-A600N
 */
fun ServerRequest.kpDevice(): Mono<String> {
    val kpDevice = this.headers().header(HEADER_KP_DEVICE)
    return if (kpDevice.isEmpty()) {
        "".toMono()
    } else {
        kpDevice[0].toMono()
    }
}

fun ServerRequest.remoteIp(): Mono<String> = this.remoteIpWithoutMono().toMono()
fun ServerRequest.remoteIpWithoutMono(): String {
    var addr = ""

    val xRealIp = this.headers().header("x-real-ip")
    if (addr.isEmpty() && !xRealIp.isEmpty()) {
        addr = xRealIp[0]
    }


    val xDaumIp = this.headers().header(REMOTE_IP)
    if (addr.isEmpty() && !xDaumIp.isEmpty()) {
        addr = xDaumIp[0]
    }

    if (addr.isEmpty()) {
        addr = "unknown"
    }

    return addr
}

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