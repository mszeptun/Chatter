package com.szeptun.common

sealed class Response<T>(open val data: T? = null, open val errorMessage: String? = null) {

    class Loading<T> : Response<T>()

    data class Success<T>(override val data: T) : Response<T>(data = data)

    data class Error<T>(override val errorMessage: String) :
        Response<T>(errorMessage = errorMessage)
}