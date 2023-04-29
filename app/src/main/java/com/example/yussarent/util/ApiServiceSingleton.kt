package com.example.yussarent.util

import com.example.yussarent.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceSingleton {

    fun createApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://yyussa.xode.rw/landlord/api/management.php/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}