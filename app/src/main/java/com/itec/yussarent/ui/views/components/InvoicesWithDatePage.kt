package com.itec.yussarent.ui.views.components

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
fun ShowInvoicesWithDate(
    roomViewModel: RoomViewModel = hiltViewModel(),
    navController: NavHostController,
    buildings: List<Building>,
) {
    val iconColor = MaterialTheme.colorScheme.primary
    var selectedBuilding by remember { mutableStateOf<Building?>(null) }
    val networkErrorDialog by  roomViewModel.buildingDueInvoicesNetworkErrorHandler.networkError.collectAsState()
    val allInvoices by roomViewModel.dueInvoices.collectAsState()
    val buildingInvoices by roomViewModel.buildingDueInvoices.collectAsState()
    var invoiceNumber by remember {
        mutableStateOf(allInvoices.size)
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
    val newJob = remember {
        coroutineScope.launch {

            roomViewModel.dueInvoicesLoading
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
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            CompanyBuildingList(buildings = buildings) {
                selectedBuilding = it
             if ((selectedBuilding?.id ?: 0) != 0) {
                    roomViewModel.getBuildingDueInvoices(
                        CountServicesSingleton.invoiceSelectedDate.format(
                            DateTimeFormatter.ISO_LOCAL_DATE
                        ),
                        buildingId = selectedBuilding!!.id.toString()
                    )
                } else {
                    roomViewModel.getDueInvoices(
                        CountServicesSingleton.invoiceSelectedDate.format(
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
                                      if ((selectedBuilding?.id ?: 0) != 0) {
                                            roomViewModel.getBuildingDueInvoices(
                                                date.format(
                                                    DateTimeFormatter.ISO_LOCAL_DATE
                                                ),
                                                buildingId = selectedBuilding!!.id.toString()
                                            )

                                        } else {
                                            roomViewModel.getDueInvoices(
                                                date.format(
                                                    DateTimeFormatter.ISO_LOCAL_DATE
                                                )
                                            )
                                        }
                                    }
                                }
                                item {
                                    invoiceNumber =if((selectedBuilding == null) || (selectedBuilding?.id == 0)){
                                        allInvoices.size
                                      }
                                    else {
                                        buildingInvoices.size
                                    }
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
                                                    }
                                           )
                                   }
                                }
                            }
                        }
                    }
                }
            }
}
