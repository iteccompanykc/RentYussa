package com.itec.yussarent.util

import com.itec.yussarent.data.models.Company

data class CompanyState(
    val isLoading : Boolean = false,
    val company: Company ? = null,
    val error : String =""
)