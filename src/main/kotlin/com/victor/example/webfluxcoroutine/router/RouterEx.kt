package com.victor.example.webfluxcoroutine.router

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.victor.example.webfluxcoroutine.model.latency.LatencyInfo
import org.slf4j.LoggerFactory
import org.slf4j.Marker
import org.slf4j.MarkerFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*


object LogMarker {
    val API_LATENCY : Marker = MarkerFactory.getMarker("API_LATENCY")!!
}

/**
 * latency 정보를 남기기 위한 filter. logging 되는 정보가 elastic search에 적재되어 확인할 수 있다.
 *
 * @see https://stackoverflow.com/questions/45240005/how-to-log-request-and-response-bodies-in-spring-webflux
 */
@Component
class LoggingFilter(private val env: Environment) : WebFilter {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        log.info("start to call to ${exchange.request.path}")

        val stopWatch = StopWatch()
        stopWatch.start()

        var filter = Mono.empty<Void>()
        try {
            filter = chain.filter(exchange)
            exchange.response.beforeCommit {
                log.info("finished call to ${exchange.request.path}.")
                loggingLantency(exchange, stopWatch)
            }
        } catch (e: Exception) {
            log.error("failed to logging latency information.", e)
            if (stopWatch.isRunning) {
                stopWatch.stop()
            }
        }
        return filter
    }

    private fun loggingLantency(exchange: ServerWebExchange, stopWatch: StopWatch): Mono<Void> {
        stopWatch.stop()
        val objectMapper = jacksonObjectMapper()
        val writer = objectMapper.writer()

        val statusCode = exchange.response.statusCode ?: HttpStatus.OK
        val apiPath = exchange.request.path.pathWithinApplication().value()

        if (apiPath.contains("ping")) {
            return Mono.empty()
        }

        val lantency = LatencyInfo(isProduction(env), apiPath, statusCode.value(), LocalDateTime.now(), stopWatch.totalTimeMillis)
        val lantencyInfo:String = writer.writeValueAsString(lantency)

        log.info(LogMarker.API_LATENCY, lantencyInfo)

        return Mono.empty()
    }
}

fun isProduction(env: Environment): Boolean {
    return Arrays.stream(env.activeProfiles)
            .anyMatch { p -> p.equals("production", ignoreCase = true) }
}