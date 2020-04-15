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

open class ErrorHandler {
    private val log = LogManager.getLogger()!!

    private fun handle(e: Throwable, serverRequest: ServerRequest, defaultErrorCd: ErrorCd = ErrorCd.INTERNAL_ERROR): Pair<Int, ErrorResponse> {
        log.warn("Fail to handle ${serverRequest.uri()}", e)
        return when (e) {
            is ApiException -> {
                val errorResponse = if (e.customMessageFlag) {
                    ErrorResponse(errorCd = e.errorCd, message = e.message!!)
                } else {
                    ErrorResponse(errorCd = e.errorCd)
                }

                log.debug("Handling ${e.javaClass.name} Responding with $errorResponse")
                Pair(e.errorCd.statusCode, errorResponse)
            }
            else -> {
                val errorResponse = ErrorResponse(errorCd = defaultErrorCd)
                log.debug("Handling ${e.javaClass.name} Responding with $errorResponse")
                Pair(defaultErrorCd.statusCode, errorResponse)
            }
        }
    }

    suspend fun coHandleException(e: Throwable, serverRequest: ServerRequest, defaultErrorCd: ErrorCd = ErrorCd.INTERNAL_ERROR): ServerResponse {
        val errorResponse = handle(e, serverRequest, defaultErrorCd)
        return ServerResponse
                .status(errorResponse.first)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValueAndAwait(errorResponse.second)
    }

    fun handleException(e: Throwable, serverRequest: ServerRequest, defaultErrorCd: ErrorCd = ErrorCd.INTERNAL_ERROR): Mono<ServerResponse> {
        val errorResponse = handle(e, serverRequest, defaultErrorCd)
        return ServerResponse
                .status(errorResponse.first)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(errorResponse.second))
    }

}

open class LoggingHandler {
    private val log = LogManager.getLogger()!!
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

open class AvoidEmptyResponseHandler {

    fun avoidEmptyResponse(): Mono<ServerResponse> = ServerResponse
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                    fromValue(ErrorResponse(ErrorCd.INTERNAL_ERROR))
            )
}

open class LoggingAndErrorHandler {
    private val errorHandler: ErrorHandler = ErrorHandler()
    private val loggingHandler: LoggingHandler = LoggingHandler()
    private val avoidEmptyResponseHandler: AvoidEmptyResponseHandler = AvoidEmptyResponseHandler()

    suspend fun coHandleException(e: Throwable, serverRequest: ServerRequest, defaultErrorCd: ErrorCd = ErrorCd.INTERNAL_ERROR): ServerResponse = errorHandler.coHandleException(e, serverRequest, defaultErrorCd)
    fun logRequest(serverRequest: ServerRequest) = loggingHandler.logRequest(serverRequest)
    fun logResponse(serverResponse: ServerResponse) = loggingHandler.logResponse(serverResponse)
    fun <Res> logResponse(serverRequest: ServerRequest, serverResponse: ServerResponse, body: Res) = loggingHandler.logResponse(serverRequest, serverResponse, body)

    fun avoidEmptyResponse(): Mono<ServerResponse> = avoidEmptyResponseHandler.avoidEmptyResponse()
}

abstract class AbstractCoHandler : LoggingAndErrorHandler() {
    protected suspend inline fun renderOrRedirectWithFormDataAndPathVariableAndHeaderInfo(
            serverRequest: ServerRequest,
            crossinline f: suspend (body: MultiValueMap<String, String>, pathVariables: Map<String, String>, headerInfo: HeaderInfo) -> AbstractHandler.RenderContext): ServerResponse {
        return coroutineScope {
            try {
                logRequest(serverRequest)
                val context = f(serverRequest.formData().awaitSingle(), serverRequest.pathVariables(), HeaderInfo.of(serverRequest))
                if (context.redirect) {
                    val builder = redirectBuilder(context.resourceName)
                    context.headers.forEach {
                        builder.header(it.key, it.value)
                    }
                    val serverResponse = builder.buildAndAwait()
                    logResponse(serverResponse)
                    serverResponse
                } else {
                    val builder = RenderingResponse.create(context.resourceName)
                            .modelAttributes(context.model)
                    context.headers.forEach {
                        builder.header(it.key, it.value)
                    }

                    val renderingResponse = builder.buildAndAwait()
                    logResponse(renderingResponse)
                    renderingResponse
                }
            } catch (e: Exception) {
                coHandleException(e, serverRequest)
            }
        }
    }

    protected suspend fun renderOrRedirectWithPathVariableAndHeaderInfo(
            serverRequest: ServerRequest,
            f: suspend (Map<String, String>, headerInfo: HeaderInfo) -> AbstractHandler.RenderContext): ServerResponse {
        return coroutineScope {
            try {
                logRequest(serverRequest)
                val context = f(serverRequest.pathVariables(), HeaderInfo.of(serverRequest))
                if (context.redirect) {
                    val builder = redirectBuilder(context.resourceName)
                    context.headers.forEach {
                        builder.header(it.key, it.value)
                    }
                    val serverResponse = builder.buildAndAwait()
                    logResponse(serverResponse)
                    serverResponse
                } else {
                    val builder = RenderingResponse.create(context.resourceName)
                            .modelAttributes(context.model)
                    context.headers.forEach {
                        builder.header(it.key, it.value)
                    }
                    builder.buildAndAwait()
                }
            } catch (e: Exception) {
                coHandleException(e, serverRequest)
            }
        }
    }

    protected inline suspend fun <reified Req, Res> handleWithRequestBody(serverRequest: ServerRequest,
                                                                  crossinline f: (Req) -> Mono<Res>): ServerResponse {
        try {
            logRequest(serverRequest)
            val body = serverRequest.bodyToMono(Req::class.java)
            val res = f(body)
            ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(res)
                    .doOnNext { logResponse(serverRequest, it, res) }.awaitSingle()
        } catch (e: Exception) {
            coHandleException(e, serverRequest)
        }
    }

    protected suspend fun <Res> handleWithHeaderInfo(serverRequest: ServerRequest, f: suspend (HeaderInfo) -> Res): ServerResponse {
        return coroutineScope {
            try {
                logRequest(serverRequest)
                val res = f(HeaderInfo.of(serverRequest))
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(res)
                        .doOnNext { logResponse(serverRequest, it, res) }.awaitSingle()
            } catch (e: Exception) {
                coHandleException(e, serverRequest)
            }
        }
    }

    protected suspend fun <RES> handleWithQueryParams(
            serverRequest: ServerRequest,
            param: Map<String, Any>,
            f: suspend (Map<String, Any>) -> RES): ServerResponse {

        return coroutineScope {
            try {
                logRequest(serverRequest)
                val res: RES = f(param, payAccount)
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(res)
                        .doOnNext { logResponse(serverRequest, it, res) }.awaitSingle()
            } catch (e: Exception) {
                coHandleException(e, serverRequest)
            }
        }
    }
}

