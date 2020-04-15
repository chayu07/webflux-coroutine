package com.victor.example.kotlinx

fun List<String>.getFirstOrEmptyString(): String =
        this.getOrElse(0) { "" }