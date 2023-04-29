package com.example.yussarent.network

import com.example.yussarent.data.models.*
import com.example.yussarent.data.models.Invoice
import com.example.yussarent.data.models.Payment
import com.example.yussarent.data.models.Room
import com.example.yussarent.data.models.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("rooms")
    suspend fun getRooms(@Query("company_id") company_id: String): List<Room>
    @GET("rooms")
    suspend fun getBuildingRooms(@Query("bu_id") bu_id: String): List<Room>
    @GET("invoices")
    suspend fun getInvoices(@Query("company_id") company_id: String): List<Invoice>
    @GET("invoices")
    suspend fun getBuildingInvoices(@Query("bu_id") bu_id: String): List<Invoice>
    @GET("due_invoices")
    suspend fun getDueInvoices(@Query("company_id") company_id: String,@Query("date") date: String): List<Invoice>
    @GET("due_invoices")
    suspend fun getBuildingDueInvoices(@Query("bu_id") bu_id: String,@Query("date") date: String): List<Invoice>
    @GET("payments")
    suspend fun getPayments(@Query("company_id") company_id: String): List<Payment>
    @GET("due_payments")
    suspend fun getDuePayments(@Query("company_id") company_id: String,@Query("date") date: String): List<Payment>
    @GET("due_payments")
    suspend fun getBuildingDuePayments(@Query("bu_id") bu_id : String,@Query("date") date: String): List<Payment>
    @GET("buildings")
    suspend fun getBuildings(@Query("company_id") company_id: String): List<Building>
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): User

    @POST("login")
    @FormUrlEncoded
    suspend fun login(@Field("username") username: String, @Field("password") password: String): Response<ResponseBody>
}

