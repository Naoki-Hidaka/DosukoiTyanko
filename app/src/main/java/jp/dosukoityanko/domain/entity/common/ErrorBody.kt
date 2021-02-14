package jp.dosukoityanko.domain.entity.common

import jp.dosukoityanko.domain.service.JsonHandler

data class ErrorBody(
    val error: List<ErrorContent>
) {
    companion object {
        fun fromJson(error: String?): ErrorBody =
            JsonHandler.converter.fromJson(error, ErrorBody::class.java)
    }

    fun errorString(): String {
        return error.joinToString("\\n") {
            it.message
        }
    }
}

data class ErrorContent(
    val code: Int,
    val message: String
)
