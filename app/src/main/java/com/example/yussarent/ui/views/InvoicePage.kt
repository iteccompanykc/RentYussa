package com.example.yussarent.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.yussarent.data.models.Invoice
import com.example.yussarent.data.models.Screen

@Composable
fun InvoiceScreen(invoices: List<Invoice>, navController: NavHostController) {
    val iconColor = MaterialTheme.colorScheme.primary
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        CardInfoView(invoices.size, "Invoices", Screen.Invoice.route,navController, icon = {
            Icon(
                imageVector = Icons.Filled.Receipt,
                contentDescription = "Invoice icon",
                tint = iconColor,
                modifier = Modifier.size(48.dp)
            )
        })
    }


}