package com.itec.yussarent.data.repositories

import com.itec.yussarent.data.models.Building
import com.itec.yussarent.data.models.Invoice
import com.itec.yussarent.data.models.Payment
import com.itec.yussarent.data.models.Room

interface RentalRepository{

   suspend fun getAvailableRooms(companyId: String): List<Room>
   suspend fun getOccupiedRooms(companyId: String): List<Room>
   suspend fun getInvoices(companyId:String): List<Invoice>
   suspend fun getDueInvoices(companyId: String,date:String): List<Invoice>
   suspend fun getPayments(companyId: String): List<Payment>
  suspend fun getDuePayments(companyId: String,date:String): List<Payment>
  suspend fun getRooms(companyId: String): List<Room>
  suspend fun getBuildings(companyId: String): List<Building>
  suspend fun getBuildingInvoices(buildingId: String,date:String): List<Invoice>
  suspend fun getBuildingRooms(buildingId: String): List<Room>
  suspend fun getBuildingOccupiedRooms(buildingId: String): List<Room>
  suspend fun getBuildingAvailableRooms(buildingId: String): List<Room>
  suspend fun getBuildingDueInvoices(buildingId: String,date:String): List<Invoice>
  suspend fun getBuildingDuePayments(buildingId: String, date: String): List<Payment>
}
