package com.victor.example.webfluxcoroutine.code

enum class ErrorCd(
        val errorMessage: String = "",
        val statusCode: Int = 500) {
    NONE("", 200),
    INTERNAL_ERROR("일시적인 시스템 오류입니다. 잠시 후 다시 이용해주세요.", 500),
    BAD_REQUEST("잘못된 요청입니다.", 400);


    fun replaceMessage(oldValue: String, newValue: String): String {
        return this.errorMessage.replace(oldValue, newValue)
    }
}
