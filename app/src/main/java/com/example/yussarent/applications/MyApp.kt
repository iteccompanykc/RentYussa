package com.example.yussarent.applications

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.yussarent.util.INetworkErrorHandler
import com.example.yussarent.util.NetworkErrorHandlerFactory
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    fun logout() {
        // Clear any user data and navigate to the login screen
    }

    override fun onCreate() {
        super.onCreate()
    }

}

