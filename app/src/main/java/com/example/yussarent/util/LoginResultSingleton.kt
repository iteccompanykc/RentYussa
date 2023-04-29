package com.example.yussarent.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object LoginResultSingleton {
    private val loginResultLiveData = MutableLiveData<LoginResult>()

    fun setLoginResult(loginResult: LoginResult) {
        loginResultLiveData.value = loginResult
    }

    fun getLoginResult(): LiveData<LoginResult> {
        return loginResultLiveData
    }
    fun resetLoginResult() {
        loginResultLiveData.value = null
    }
}