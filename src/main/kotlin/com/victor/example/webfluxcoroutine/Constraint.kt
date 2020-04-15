package com.victor.example.webfluxcoroutine


const val HEADER_USER_AGENT = "User-Agent"
const val EMPTY_STRING = ""
const val SPACE = " "
const val EMPTY = "EMPTY"
const val TRUE = "TRUE"
const val FALSE = "FALSE"
const val ZERO = "0"
const val NONE = "NONE"


class HttpConnectionOptions {
    companion object {
        const val CONN_TIMEOUT_IN_MILLIES = 2000
        const val READ_TIMEOUT_IN_MILLIES = 2000
    }
}