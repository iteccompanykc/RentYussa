package com.example.yussarent.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import java.io.IOException

object NetworkErrorHandlerFactory {
    private val instances = mutableMapOf<String, NetworkErrorHandler>()

    fun create(tag: String): NetworkErrorHandler {
        return instances.getOrPut(tag) { NetworkErrorHandler() }
    }

    class NetworkErrorHandler : INetworkErrorHandler{
        private val _networkError = MutableStateFlow<String?>(null)
        override val networkError: StateFlow<String?> = _networkError

        override fun handleError(throwable: Throwable?) {
            val errorMessage = when (throwable) {
                is IOException ->"No Internet Connection found.\n" +
                        "Check your connection or try again."
                is HttpException -> "HTTP Error: ${throwable.code()}"
                else -> "Unexpected Error Occurred"
            }
            _networkError.value = errorMessage
        }

        override fun resetError() {
            _networkError.value = null
        }
    }
}
