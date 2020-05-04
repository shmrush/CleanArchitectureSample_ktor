package controller

data class Response<T>(
    val statusCode: Int,
    val data: T? = null,
    val errorMessage: String? = null
)
