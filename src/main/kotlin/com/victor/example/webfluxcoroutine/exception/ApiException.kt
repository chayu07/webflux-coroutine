package com.victor.example.webfluxcoroutine.exception

import com.victor.example.webfluxcoroutine.code.ErrorCd

class ApiException : RuntimeException {

    val errorCd: ErrorCd
    var customMessageFlag: Boolean = false

    constructor(errorCd: ErrorCd) : super(errorCd.errorMessage) {
        this.errorCd = errorCd
    }

    constructor(errorCd: ErrorCd, message: String) : super(message) {
        this.errorCd = errorCd
    }

    constructor(errorCd: ErrorCd, message: String, customMessageFlag: Boolean) : super(message) {
        this.errorCd = errorCd
        this.customMessageFlag = customMessageFlag
    }

    constructor(errorCd: ErrorCd, cause: Throwable) : super(cause) {
        this.errorCd = errorCd
    }

    constructor(errorCd: ErrorCd, message: String, cause: Throwable) : super(message, cause) {
        this.errorCd = errorCd
    }

}
