package com.example.yussarent.data.repositories.impl

import com.example.yussarent.data.models.Building
import com.example.yussarent.data.models.Invoice
import com.example.yussarent.data.models.Payment
import com.example.yussarent.data.models.Room
import com.example.yussarent.data.repositories.RentalRepository
import com.example.yussarent.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RentalRepositoryImpl(private val apiService: ApiService) : RentalRepository {

    override suspend fun getAvailableRooms(companyId: String): List<Room> {
        return withContext(Dispatchers.IO) {
            apiService.getRooms(companyId).filter { !it.isOccupied }
        }
    }
    override suspend fun getDueInvoices(companyId: String, date:String): List<Invoice> {
        return withContext(Dispatchers.IO) {
             apiService.getDueInvoices(companyId,date)
        }
    }
    override suspend fun getOccupiedRooms(companyId: String): List<Room> {
        return withContext(Dispatchers.IO) {
            apiService.getRooms(companyId).filter { it.isOccupied }
        }
    }


    override suspend fun getInvoices(companyId: String): List<Invoice> {
        return withContext(Dispatchers.IO) {
            apiService.getInvoices(companyId)
        }
    }

    override suspend fun getPayments(companyId: String): List<Payment> {
        return withContext(Dispatchers.IO) {
            apiService.getPayments(companyId)
        }
    }

    override suspend fun getDuePayments(companyId: String, date: String): List<Payment> {
       return  withContext(Dispatchers.IO) {
           apiService.getDuePayments(companyId,date)
       }
    }

    override suspend fun getRooms(companyId: String): List<Room> {
        return withContext(Dispatchers.IO) {
            apiService.getRooms(companyId)
        }
    }

    override suspend fun getBuildings(companyId: String): List<Building> {
     return withContext(Dispatchers.IO){
         apiService.getBuildings(companyId)
     }
    }
    override suspend fun getBuildingInvoices(buildingId: String, date: String): List<Invoice> {
        return withContext(Dispatchers.IO){
            apiService.getBuildingInvoices(bu_id = buildingId)
        }
    }

    override suspend fun getBuildingRooms(buildingId: String): List<Room> {
        return withContext(Dispatchers.IO){
            apiService.getBuildingRooms(bu_id = buildingId)
        }
    }

    override suspend fun getBuildingOccupiedRooms(buildingId: String): List<Room> {
      return withContext(Dispatchers.IO){
          apiService.getBuildingRooms(buildingId).filter { it.isOccupied }
      }
    }

    override suspend fun getBuildingAvailableRooms(buildingId: String): List<Room> {
        return withContext(Dispatchers.IO){
            apiService.getBuildingRooms(buildingId).filter { !it.isOccupied }
        }
    }

    override suspend fun getBuildingDueInvoices(buildingId: String, date: String): List<Invoice> {
      return withContext(Dispatchers.IO){
          apiService.getBuildingDueInvoices(buildingId,date)
      }
    }

    override suspend fun getBuildingDuePayments(buildingId: String, date: String): List<Payment> {
        return withContext(Dispatchers.IO){
            apiService.getBuildingDuePayments(buildingId, date)
        }

    }
}
