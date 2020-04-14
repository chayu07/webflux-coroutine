package com.victor.example.kotlinx

import com.victor.example.webfluxcoroutine.EMPTY_STRING
import com.victor.example.webfluxcoroutine.code.ErrorCd
import com.victor.example.webfluxcoroutine.exception.ApiException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*


fun <T> Optional<T>.errorIfEmpty(): Mono<T> {
    return if (this.isPresent) {
        Mono.just(this.get())
    } else {
        Mono.error(ApiException(ErrorCd.INTERNAL_ERROR))
    }
}

fun Optional<String>.orEmptyString(): String =
        this.orElse(EMPTY_STRING)

fun <T> Optional<T>.orEmptyMono(): Mono<Optional<T>> {
    return if (this.isPresent) {
        this.toMono()
    } else {
        Mono.empty()
    }
}