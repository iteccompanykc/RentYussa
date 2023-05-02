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
import com.example.yussarent.util.NetworkError
import com.example.yussarent.viewModels.RoomViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter


@OptIn(FlowPreview::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowPaymentsWithDate(roomViewModel: RoomViewModel, navController: NavHostController) {
    val iconColor = MaterialTheme.colorScheme.primary
    val duePayments by roomViewModel.duePayments.collectAsState(emptyList())
    var isLoading by remember {
        mutableStateOf(false)
    }
    var paymentNumber by remember {
        mutableStateOf(duePayments.size)
    }
    val networkErrorDialog by roomViewModel.buildingDuePaymentNetworkErrorHandler.networkError.collectAsState()
    val coroutineScope = remember { CoroutineScope(Dispatchers.Main) }
    val job = remember {
        coroutineScope.launch {

            roomViewModel.dueBuildingPaymentsLoading
                .debounce(1000)
                .collectLatest { loading ->
                    isLoading = loading
                }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            job.cancel()
            isLoading=false
        }
    }

    val buildings by roomViewModel.buildings.collectAsState(emptyList())
    var selectedBuilding by remember { mutableStateOf<Building?>(null) }


        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            CompanyBuildingList(buildings = buildings) {
                selectedBuilding = it
                paymentNumber = if ((selectedBuilding?.id ?: 0) != 0) {
                    roomViewModel.getBuildingDuePayments(
                        CountServicesSingleton.paymentSelectedDate.format(
                            DateTimeFormatter.ISO_LOCAL_DATE
                        ),
                        buildingId = selectedBuilding!!.id.toString()
                    )
                    roomViewModel.dueBuildingPayment.value.size
                } else {
                    roomViewModel.getDuePayments(
                        CountServicesSingleton.invoiceSelectedDate.format(
                            DateTimeFormatter.ISO_LOCAL_DATE
                        )
                    )
                    duePayments.size
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
                    if (networkErrorDialog != null) {
                        InfoDialog(
                            title = "Whoops!",
                            desc = networkErrorDialog,
                            onRetry = {
                                roomViewModel.buildingDuePaymentNetworkErrorHandler.resetError()
                                roomViewModel.refresh()
                            },
                            onCancel = {
                                roomViewModel.buildingDuePaymentNetworkErrorHandler.resetError()
                            }
                        )
                    } else {
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
                                            paymentNumber = if ((selectedBuilding?.id ?: 0) != 0) {
                                                roomViewModel.getBuildingDuePayments(
                                                    CountServicesSingleton.paymentSelectedDate.format(
                                                        DateTimeFormatter.ISO_LOCAL_DATE
                                                    ),
                                                    buildingId = selectedBuilding!!.id.toString()
                                                )
                                                roomViewModel.dueBuildingPayment.value.size
                                            } else {
                                                roomViewModel.getDuePayments(
                                                    CountServicesSingleton.invoiceSelectedDate.format(
                                                        DateTimeFormatter.ISO_LOCAL_DATE
                                                    )
                                                )
                                                duePayments.size
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

}