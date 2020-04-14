package com.victor.example.kotlinx

import com.victor.example.webfluxcoroutine.EMPTY_STRING

fun List<String>.getFirstOrEmptyString(): String =
        this.getOrElse(0) { EMPTY_STRING }