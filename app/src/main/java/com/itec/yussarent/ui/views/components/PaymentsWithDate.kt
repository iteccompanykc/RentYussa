package com.itec.yussarent.ui.views.components

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itec.yussarent.data.models.Building
import com.itec.yussarent.data.models.Screen
import com.itec.yussarent.util.CountServicesSingleton
import com.itec.yussarent.viewModels.RoomViewModel
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
fun ShowPaymentsWithDate(
    roomViewModel: RoomViewModel = hiltViewModel(),
    navController: NavHostController,
    buildings: List<Building>
) {
    val iconColor = MaterialTheme.colorScheme.primary
    val duePayments by roomViewModel.duePayments.collectAsState()
    val buildingPayment by roomViewModel.dueBuildingPayment.collectAsState()
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

            roomViewModel.duePaymentsLoading
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
    val newJob = remember {
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
            newJob.cancel()
            isLoading=false
        }
    }
    var selectedBuilding by remember { mutableStateOf<Building?>(null) }


        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            CompanyBuildingList(buildings = buildings) {
                selectedBuilding = it

                 if ((selectedBuilding?.id ?: 0) != 0) {
                    roomViewModel.getBuildingDuePayments(
                        CountServicesSingleton.paymentSelectedDate.format(
                            DateTimeFormatter.ISO_LOCAL_DATE
                        ),
                        buildingId = selectedBuilding!!.id.toString()
                    )
                } else {
                    roomViewModel.getDuePayments(
                        CountServicesSingleton.paymentSelectedDate.format(
                            DateTimeFormatter.ISO_LOCAL_DATE
                        )
                    )
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
                                           if ((selectedBuilding?.id ?: 0) != 0) {
                                                roomViewModel.getBuildingDuePayments(
                                                    CountServicesSingleton.paymentSelectedDate.format(
                                                        DateTimeFormatter.ISO_LOCAL_DATE
                                                    ),
                                                    buildingId = selectedBuilding!!.id.toString()
                                                )

                                            } else {
                                                roomViewModel.getDuePayments(
                                                    CountServicesSingleton.paymentSelectedDate.format(
                                                        DateTimeFormatter.ISO_LOCAL_DATE
                                                    )
                                                )

                                            }
                                        }
                                    )

                                }
                                item {
                                    paymentNumber =if((selectedBuilding == null) || (selectedBuilding?.id == 0)){
                                        duePayments.size
                                    }
                                    else {
                                        buildingPayment.size
                                    }

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