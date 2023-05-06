package com.itec.yussarent.data.repositories.impl

import com.itec.yussarent.data.models.User
import com.itec.yussarent.data.repositories.UserRepository
import com.itec.yussarent.network.ApiService
import com.google.gson.Gson
import com.itec.yussarent.data.models.Company
import com.itec.yussarent.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.security.auth.login.LoginException

class UserRepositoryImpl(private val apiService: ApiService) : UserRepository {

    override suspend fun login(username: String, password: String): User? {

            val response = apiService.login(username, password)
            if (response.isSuccessful) {
                val responseData = response.body()?.string() ?: ""
                // Parse the JSON response manually to retrieve the user data
                return Gson().fromJson(responseData, User::class.java)
            } else {
                throw LoginException("Login failed: ${response.code()}")
            }
        }
        override suspend fun getCompany(companyId: String): Flow<Resource<Company>> {
            return withContext(Dispatchers.IO) {
                flow {
                    try {
                        emit(Resource.Loading<Company>())
                        val company = apiService.getCompany(companyId)
                        emit(Resource.Success<Company>(company))
                    } catch (e: HttpException) {
                        emit(
                            Resource.Error<Company>(
                                e.localizedMessage ?: "An unexpected error occurred "
                            )
                        )
                    } catch (ex: IOException) {
                        emit(Resource.Error<Company>("Couldn't reach server please check your internet connection "))
                    }
                }
            }
        }
}
