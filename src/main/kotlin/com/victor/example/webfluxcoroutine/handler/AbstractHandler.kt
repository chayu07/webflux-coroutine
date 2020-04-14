package com.victor.example.webfluxcoroutine.handler


import com.victor.example.webfluxcoroutine.code.ErrorCd
import com.victor.example.webfluxcoroutine.exception.ApiException
import com.victor.example.kotlinx.toPrettyFormat
import com.victor.example.kotlinx.userAgent
import com.victor.example.webfluxcoroutine.model.ErrorResponse
import com.victor.example.webfluxcoroutine.model.HeaderInfo
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

abstract class AbstractHandler {

    private val log = LogManager.getLogger()!!

    protected inline fun <reified Req, Res> handleWithRequestBody(serverRequest: ServerRequest,
                                                                  crossinline f: (Req) -> Mono<Res>): Mono<ServerResponse> {
        logRequest(serverRequest)
        return serverRequest.bodyToMono(Req::class.java)
                .flatMap { req -> f(req) }
                .flatMap { res ->
                    ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(res))
                            .doOnNext { logResponse(serverRequest, it, res) }
                }
                .onErrorResume { e -> this.handleException(e, serverRequest) }
                .switchIfEmpty(avoidEmptyResponse())

    }

    protected fun handleException(e: Throwable, serverRequest: ServerRequest, defaultErrorCd: ErrorCd = ErrorCd.INTERNAL_ERROR): Mono<ServerResponse> {
        log.warn("Fail to handle ${serverRequest.uri()}", e)
        return when (e) {
            is ApiException -> {
                val errorResponse = if (e.customMessageFlag) {
                    ErrorResponse(errorCd = e.errorCd, message = e.message!!)
                } else {
                    ErrorResponse(errorCd = e.errorCd)
                }

                log.debug("Handling ${e.javaClass.name} Responding with $errorResponse")
                ServerResponse
                        .status(e.errorCd.statusCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(errorResponse))
            }
            else -> {
                val errorResponse = ErrorResponse(errorCd = defaultErrorCd)
                log.debug("Handling ${e.javaClass.name} Responding with $errorResponse")
                ServerResponse
                        .status(defaultErrorCd.statusCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(errorResponse))
            }
        }
    }

    protected fun avoidEmptyResponse(): Mono<ServerResponse> = ServerResponse
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                    fromValue(ErrorResponse(ErrorCd.INTERNAL_ERROR))
            )


    fun logRequest(serverRequest: ServerRequest) {
        log.info("""
            |Handling Request ========================================
            |Method: ${serverRequest.method()}
            |Uri: ${serverRequest.uri()}
            |Headers:
            |${serverRequest.headers().asHttpHeaders().toPrettyFormat()}
            |========================================
        """.trimMargin())
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun <Res> logResponse(serverRequest: ServerRequest, serverResponse: ServerResponse, body: Res) {
        log.info("""
            |Responding with ========================================
            |
            |Handling Request ========================================
            |Method: ${serverRequest.method()}
            |Uri: ${serverRequest.uri()}
            |Headers:
            |${serverRequest.headers().asHttpHeaders().toPrettyFormat()}
            |
            |Response ========================================
            |Status: ${serverResponse.statusCode()}
            |Headers:
            |${serverResponse.headers().toPrettyFormat()}
            |Body:
            |${body.toString()}
            |========================================
        """.trimMargin())
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun logResponse(serverResponse: ServerResponse) {
        log.info("""
            |Responding with ========================================
            |Status: ${serverResponse.statusCode()}
            |Headers:
            |${serverResponse.headers().toPrettyFormat()}
            |Cookies:
            |${serverResponse.cookies().toPrettyFormat()}
            |========================================
        """.trimMargin())
    }

}

