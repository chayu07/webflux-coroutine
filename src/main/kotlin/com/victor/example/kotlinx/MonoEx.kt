package com.victor.example.kotlinx

import com.victor.example.webfluxcoroutine.code.ErrorCd
import com.victor.example.webfluxcoroutine.exception.ApiException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import reactor.retry.RetryContext
import reactor.retry.retryExponentialBackoff
import java.time.Duration
import java.util.*

/**
 * 특정 데이터 없이, 이벤트를 다음 fn으로 넘기기 위한 helper method.
 */
fun confirmedRequest(): Mono<Unit> = Unit.toMono()

fun <T> emptyOptionalMono(): Mono<Optional<T>> = Optional.empty<T>().toMono()

fun <T> Mono<T>.retryExponentialBackoffEx(times: Long, delay: Duration, maxDelay: Duration? = null, doOnRetry: ((RetryContext<T>) -> Unit)? = null): Mono<T> {
    return this.retryExponentialBackoff(times, delay, maxDelay, doOnRetry = doOnRetry).toMono()
}

fun <T> Mono<Optional<T>>.onErrorEmpty(): Mono<Optional<T>> {
    return this.onErrorResume { emptyOptionalMono() }
}

fun <T> errorChange(origin: Throwable, changedError: Mono<T>, condition: (Throwable) -> Boolean): Mono<T> {
    return if (condition.invoke(origin)) {
        changedError
    } else {
        origin.toMono()
    }
}

fun doNothing(): Mono<Unit> = Mono.just(Unit)

fun <T> unknownError(message: String, errorCd: ErrorCd = ErrorCd.INTERNAL_ERROR): Mono<T> =
        ApiException(errorCd, message).toMono()
