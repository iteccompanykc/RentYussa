package com.example.yussarent.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.yussarent.data.repositories.impl.RentalRepositoryImpl

class RentalWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val rentalRepository: RentalRepositoryImpl
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Call the method in the rental repository to get the list of available rooms
            val availableRooms = rentalRepository.getAvailableRooms("9")

            // Do something with the available rooms

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
