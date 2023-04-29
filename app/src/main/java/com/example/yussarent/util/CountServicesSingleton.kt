package com.example.yussarent.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
object CountServicesSingleton {
    var invoiceSelectedDate: LocalDate by mutableStateOf(LocalDate.now())
    var paymentSelectedDate: LocalDate by mutableStateOf(LocalDate.now())
}