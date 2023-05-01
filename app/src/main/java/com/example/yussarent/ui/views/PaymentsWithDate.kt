package com.example.yussarent.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.yussarent.data.models.Building
import com.example.yussarent.data.models.Screen
import com.example.yussarent.util.CountServicesSingleton
import com.example.yussarent.viewModels.RoomViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowPaymentsWithDate(roomViewModel: RoomViewModel, navController: NavHostController) {
    val iconColor = MaterialTheme.colorScheme.primary
    val payments by roomViewModel.duePayments.collectAsState(emptyList())
    val buildings by roomViewModel.buildings.collectAsState(emptyList())

    var selectedBuilding by remember { mutableStateOf<Building?>(null) }
    Column(  verticalArrangement = Arrangement.spacedBy(2.dp),) {
            CompanyBuildingList(buildings = buildings){
                selectedBuilding=it
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
                        selectedDate = CountServicesSingleton.paymentSelectedDate,
                        onDateSelected = { date ->
                            CountServicesSingleton.paymentSelectedDate = date
                            roomViewModel.getDuePayments(
                                CountServicesSingleton.paymentSelectedDate.format(
                                    DateTimeFormatter.ISO_LOCAL_DATE
                                )
                            )
                        }
                    )

                }
                item {
                    CardInfoView(payments.size, "Payments", Screen.Payments.route,navController, icon = {
                        Icon(
                            imageVector = Icons.Filled.ThumbUp,
                            contentDescription = "Payment icon",
                            tint = iconColor,
                            modifier = Modifier.size(48.dp)
                        )
                    })
                }
            }
        }
    }


}