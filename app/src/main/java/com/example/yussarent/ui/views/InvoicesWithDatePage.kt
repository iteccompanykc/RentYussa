package com.example.yussarent.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
fun ShowInvoicesWithDate(roomViewModel: RoomViewModel, navController: NavHostController) {
    val iconColor = MaterialTheme.colorScheme.primary
    val invoices by roomViewModel.dueInvoices.collectAsState(emptyList())
    val buildings by roomViewModel.buildings.collectAsState(emptyList())
    var selectedBuilding by remember { mutableStateOf<Building?>(null) }
    val networkErrorDialog by  roomViewModel.buildingDueInvoicesNetworkErrorHandler.networkError.collectAsState()
    var invoiceNumber by remember {
        mutableStateOf(invoices.size)
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    val coroutineScope = remember { CoroutineScope(Dispatchers.Main) }
    val job = remember {
        coroutineScope.launch {

            roomViewModel.buildingDueInvoicesLoading
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
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            CompanyBuildingList(buildings = buildings) {
                selectedBuilding = it
                invoiceNumber = if ((selectedBuilding?.id ?: 0) != 0) {
                    roomViewModel.getBuildingDueInvoices(
                        CountServicesSingleton.paymentSelectedDate.format(
                            DateTimeFormatter.ISO_LOCAL_DATE
                        ),
                        buildingId = selectedBuilding!!.id.toString()
                    )
                    roomViewModel.buildingDueInvoices.value.size

                } else {
                    roomViewModel.getDueInvoices(
                        CountServicesSingleton.invoiceSelectedDate.format(
                            DateTimeFormatter.ISO_LOCAL_DATE
                        )
                    )
                    invoices.size
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
                                roomViewModel.buildingDueInvoicesNetworkErrorHandler.resetError()
                                roomViewModel.refresh()
                            },
                            onCancel = {
                                roomViewModel.buildingDueInvoicesNetworkErrorHandler.resetError()
                            }
                        )
                    } else {
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(align = Alignment.Center)
                                    .background(Color.White)
                                    .shadow(16.dp),
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
                                        selectedDate = CountServicesSingleton.invoiceSelectedDate
                                    ) { date ->
                                        CountServicesSingleton.invoiceSelectedDate = date
                                        invoiceNumber = if ((selectedBuilding?.id ?: 0) != 0) {
                                            roomViewModel.getBuildingDueInvoices(
                                                CountServicesSingleton.paymentSelectedDate.format(
                                                    DateTimeFormatter.ISO_LOCAL_DATE
                                                ),
                                                buildingId = selectedBuilding!!.id.toString()
                                            )

                                            roomViewModel.buildingDueInvoices.value.size
                                        } else {
                                            roomViewModel.getDueInvoices(
                                                CountServicesSingleton.invoiceSelectedDate.format(
                                                    DateTimeFormatter.ISO_LOCAL_DATE
                                                )
                                            )

                                            invoices.size
                                        }
                                    }
                                }
                                item {
                                    CardInfoView(
                                        invoiceNumber,
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
        }
}
