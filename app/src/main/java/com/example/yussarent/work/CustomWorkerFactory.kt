package com.example.yussarent.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.yussarent.data.repositories.impl.RentalRepositoryImpl
import com.example.yussarent.data.repositories.impl.UserRepositoryImpl
import com.example.yussarent.network.ApiService
import javax.inject.Inject

class CustomWorkerFactory @Inject constructor(
    private val rentalRepository: RentalRepositoryImpl,
    private val userRepository: UserRepositoryImpl,
    private val apiService: ApiService
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when (workerClassName) {
            RentalWorker::class.java.name -> {
                RentalWorker(appContext, workerParameters, rentalRepository)
            }
            // Add other worker classes here if needed
            else -> {
                // Return null if the worker class is not supported
                null
            }
        }
    }
}

