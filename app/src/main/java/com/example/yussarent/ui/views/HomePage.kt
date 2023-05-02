package com.example.yussarent.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.yussarent.data.models.Invoice
import com.example.yussarent.data.models.Payment
import com.example.yussarent.data.models.Room
import com.example.yussarent.data.models.Screen
import com.example.yussarent.util.NetworkError
import com.example.yussarent.viewModels.RoomViewModel

@Composable
fun HomeScreen(
    roomsData: Map<String, List<Room>>,
    invoices: List<Invoice>,
    payments: List<Payment>,
    navController: NavHostController,
    roomViewModel : RoomViewModel
) {
    val networkErrorDialog by NetworkError.networkError.collectAsState()
    if (networkErrorDialog != null) {
        InfoDialog(
            title = "Whoops!",
            desc = networkErrorDialog,
            onRetry = {
                NetworkError.resetError()
                roomViewModel.refresh()
            },
            onCancel = {
                NetworkError.resetError()
            }
        )
    }
    else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                userScrollEnabled = true
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .clickable {
                                // Handle click for "All Rooms" item
                            }
                    ) {
                        roomsData["Total"]?.let { AllRoomsScreen(it, navController) }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Screen.AvailableRooms.route)
                            }
                    ) {
                        roomsData["available"]?.let {
                            AvailableRoomsScreen(
                                it,
                                roomViewModel = null,
                                navController
                            )
                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Screen.OccupiedRooms.route)
                            }
                    ) {
                        roomsData["occupied"]?.let {
                            OccupiedRoomsScreen(
                                it,
                                roomViewModel = null,
                                navController
                            )
                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Screen.Payments.route)
                            }
                    ) {
                        PaymentScreen(payments, navController)
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .width(300.dp)
                            .clickable {
                                navController.navigate(Screen.Invoice.route)
                            }
                    ) {
                        InvoiceScreen(invoices, navController)
                    }
                }

            }
        }
    }
}
