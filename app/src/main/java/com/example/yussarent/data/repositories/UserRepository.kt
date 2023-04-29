package com.example.yussarent.data.repositories

import com.example.yussarent.data.models.User

interface UserRepository {

     suspend fun login(username: String, password: String): User?

}



