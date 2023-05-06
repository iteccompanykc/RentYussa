package com.itec.yussarent.data.models

import java.io.Serializable

data class User(
    val acc_id: String,
    val l_name: String,
    val f_name: String,
    val username: String,
    val role_id: String,
    val co_id: String,
    val site_id: String,
    val dep_id: String,
    val mobile: String,
    val side:String,
): Serializable


