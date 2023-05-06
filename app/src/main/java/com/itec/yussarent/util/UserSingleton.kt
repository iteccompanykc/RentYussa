package com.itec.yussarent.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.itec.yussarent.data.models.User

object UserSingleton {
    var user: User? by mutableStateOf(null)
}