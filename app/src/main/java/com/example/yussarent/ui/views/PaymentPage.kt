package com.example.yussarent.ui.views

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.yussarent.data.models.Payment
import com.example.yussarent.data.models.Screen

@Composable
fun PaymentScreen(payments:List<Payment>, navController: NavHostController) {
    val iconColor = MaterialTheme.colorScheme.primary
    CardInfoView(payments.size,"Payments", Screen.Payments.route,navController, icon = {
        Icon(
            imageVector = Icons.Filled.ThumbUp,
            contentDescription = "Payment icon",
            tint = iconColor,
            modifier = Modifier.size(48.dp)
        )
    })
}
