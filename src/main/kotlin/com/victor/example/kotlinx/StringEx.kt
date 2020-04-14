package com.victor.example.kotlinx

import com.google.common.net.UrlEscapers
import org.apache.commons.lang3.StringUtils
import java.time.LocalDateTime
import java.time.ZoneId

fun String.removeDash() = this.replace("-", "").trim()
fun String.urlEncode(): String = UrlEscapers.urlFragmentEscaper().escape(this)

fun <T> List<T>.toPair(): Pair<T, T> {
    require(this.size == 2) { "List is not of length 2!" }
    return Pair(this[0], this[1])
}


fun String.formatBirthday(): String {
    return this.replaceFirst("(\\d{4})(\\d{2})(\\d+)".toRegex(), "$1.$2.$3")
}

fun String.formatPhoneNo(): String {
    if (StringUtils.isEmpty(this) || this.length < 9) {
        return ""
    }

    return if (this.length < 11) {
        this.replace("(\\d{3})(\\d{3})(\\d{3,4})".toRegex(), "$1-$2-$3")
    } else this.replace("(\\d{3})(\\d{4})(\\d{4})".toRegex(), "$1-$2-$3")
}


fun String.ifEmpty(f: (String) -> String): String {
    if (this.isBlank()) {
        return f(this)
    }
    return this
}

/**
 * LocalDateTime을 millesecond로 변환.
 */
fun LocalDateTime.toMilli() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
