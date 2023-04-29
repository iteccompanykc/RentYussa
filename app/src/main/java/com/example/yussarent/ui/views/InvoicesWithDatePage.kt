package com.example.yussarent.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.yussarent.data.models.Building
import com.example.yussarent.data.models.Screen
import com.example.yussarent.util.CountServicesSingleton
import com.example.yussarent.viewModels.RoomViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowInvoicesWithDate(roomViewModel: RoomViewModel, navController: NavHostController) {
    val iconColor = MaterialTheme.colorScheme.primary
    val invoices by roomViewModel.dueInvoices.observeAsState()
    val buildings by roomViewModel.buildings.observeAsState()

    var selectedBuilding by remember { mutableStateOf<Building?>(null) }

    Column(  verticalArrangement = Arrangement.spacedBy(2.dp)) {
        buildings?.let { buildings1 ->
            CompanyBuildingList(buildings = buildings1){
                selectedBuilding=it
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                userScrollEnabled = true
            ) {
                item {
                    DatePickerButton(
                        selectedDate = CountServicesSingleton.invoiceSelectedDate,
                        onDateSelected = { date ->
                            CountServicesSingleton.invoiceSelectedDate = date
                            roomViewModel.getDueInvoices(
                                CountServicesSingleton.invoiceSelectedDate.format(
                                    DateTimeFormatter.ISO_LOCAL_DATE
                                )
                            )
                        }
                    )
                }
                item {
                    invoices?.let {
                        CardInfoView(
                            it.size,
                            "Invoices",
                            Screen.Invoice.route,
                            navController,
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = "Invoice icon",
                                    tint = iconColor,
                                    modifier = Modifier.size(48.dp)
                                )
                            })
                    }
                }
            }
        }
    }
}