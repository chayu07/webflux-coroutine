package com.victor.example.webfluxcoroutine.model.latency

import java.io.Serializable
import java.net.InetAddress
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



data class Response(val status: Int, val error: String = "") : Serializable

data class LatencyInfo(private val inProduction: Boolean, val url: String, val statusCode:Int, private val from: LocalDateTime, val elapsedTime: Long) : Serializable {
    var host = "example.com"
    var phase = "sandbox"
    @Suppress("unused")
    val serviceName = "example-service"
    @Suppress("MemberVisibilityCanBePrivate")
    val requestTime: String
    @Suppress("unused")
    var response: Response = Response(statusCode)

    init {
        val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        this.requestTime = from.format(timeFormatter)
        this.phase = if (inProduction) "production" else "sandbox"
        val iAddress = InetAddress.getLocalHost()
        this.host = iAddress.hostName
    }
}

