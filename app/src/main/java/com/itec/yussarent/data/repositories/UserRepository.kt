package com.itec.yussarent.data.repositories

import com.itec.yussarent.data.models.Company
import com.itec.yussarent.data.models.User
import com.itec.yussarent.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface UserRepository {

     suspend fun login(username: String, password: String): User?
     suspend fun getCompany(companyId: String): Flow<Resource<Company>>
}



