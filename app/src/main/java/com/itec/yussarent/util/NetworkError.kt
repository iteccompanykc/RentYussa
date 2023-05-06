package com.itec.yussarent.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import java.io.IOException

object NetworkError {
    private val _networkError = MutableStateFlow<String?>(null)
    val networkError: StateFlow<String?> = _networkError
    fun handleError(throwable: Throwable?) {
        val errorMessage = when (throwable) {
            is IOException ->"No Internet Connection found.\n" +
                    "Check your connection and try again."
            is HttpException -> "HTTP Error: ${throwable.code()}"
            else -> "Unexpected Error Occurred"
        }
        _networkError.value = errorMessage
    }

    fun resetError() {
        _networkError.value = null
    }
}


