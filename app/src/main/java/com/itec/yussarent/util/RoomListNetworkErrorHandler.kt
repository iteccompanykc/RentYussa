package com.itec.yussarent.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import java.io.IOException

class RoomListNetworkErrorHandler : INetworkErrorHandler {
    private val _networkError = MutableStateFlow<String?>(null)
    override val networkError: StateFlow<String?> = _networkError

    override fun handleError(throwable: Throwable?) {
        val errorMessage = when (throwable) {
            is IOException -> "No Internet Connection found.\nCheck your connection or try again."
            is HttpException -> "HTTP Error: ${throwable.code()}"
            else -> "Unexpected Error Occurred"
        }
        _networkError.value = errorMessage
    }

    override fun resetError() {
        _networkError.value = null
    }
}
