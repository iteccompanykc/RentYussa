package com.example.yussarent.util

import kotlinx.coroutines.flow.StateFlow

interface INetworkErrorHandler {
    val networkError: StateFlow<String?>
    fun handleError(throwable: Throwable?)
    fun resetError()
}
