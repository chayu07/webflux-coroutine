package com.victor.example.webfluxcoroutine.model

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.victor.example.webfluxcoroutine.code.ErrorCd


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class ErrorResponse(val errorCode: String,
                         val errorMessage: String) {

    constructor(errorCd: ErrorCd) : this(errorCd.toString(), errorCd.errorMessage)
    constructor(errorCd: ErrorCd, message: String) : this(errorCd.toString(), message)
}

