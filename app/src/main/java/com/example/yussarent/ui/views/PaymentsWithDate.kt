package com.example.yussarent.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.yussarent.data.models.Building
import com.example.yussarent.data.models.Screen
import com.example.yussarent.util.CountServicesSingleton
import com.example.yussarent.viewModels.RoomViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowPaymentsWithDate(roomViewModel: RoomViewModel, navController: NavHostController) {
    val iconColor = MaterialTheme.colorScheme.primary
    val duePayments by roomViewModel.duePayments.collectAsState(emptyList())
    val isLoading by roomViewModel.dueBuildingPaymentsLoading.collectAsState()
    var paymentNumber by remember {
        mutableStateOf(duePayments.size)
    }
    val buildings by roomViewModel.buildings.collectAsState(emptyList())
    val duePaymentsScope = rememberCoroutineScope()
    var selectedBuilding by remember { mutableStateOf<Building?>(null) }
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            CompanyBuildingList(buildings = buildings){
                selectedBuilding=it
        }
        LaunchedEffect(roomViewModel){
            if(selectedBuilding!=null){
                if(selectedBuilding!!.id!=0){
                    roomViewModel.getBuildingDuePayments(
                        CountServicesSingleton.paymentSelectedDate.format(
                            DateTimeFormatter.ISO_LOCAL_DATE
                        ),
                        buildingId = selectedBuilding!!.id.toString()
                    )
                }
            }
            duePaymentsScope.launch {
                roomViewModel.duePayments.collectLatest { dueBuildingPayments ->
                    paymentNumber=dueBuildingPayments.size
                }
            }
        }
        Surface(
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center)
                .shadow(if (isLoading) 16.dp else 0.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    Box(
                        modifier = Modifier.size(128.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        DialogBoxLoading()
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        userScrollEnabled = true
                    ) {

                        item {
                            DatePickerButton(
                                selectedDate = CountServicesSingleton.paymentSelectedDate,
                                onDateSelected = { date ->
                                    CountServicesSingleton.paymentSelectedDate = date
                                    if ((selectedBuilding?.id ?: 0) != 0) {
                                        roomViewModel.getBuildingDuePayments(
                                            CountServicesSingleton.paymentSelectedDate.format(
                                                DateTimeFormatter.ISO_LOCAL_DATE
                                            ),
                                            buildingId = selectedBuilding!!.id.toString()
                                        )

                                    } else {
                                        roomViewModel.getDuePayments(
                                            CountServicesSingleton.invoiceSelectedDate.format(
                                                DateTimeFormatter.ISO_LOCAL_DATE
                                            )
                                        )
                                    }
                                }
                            )

                        }
                        item {
                            CardInfoView(
                                paymentNumber,
                                "Payments",
                                Screen.Payments.route,
                                navController,
                                icon = {
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
    }

}