package com.itec.yussarent.ui.views.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itec.yussarent.data.models.Building
import com.itec.yussarent.data.models.Screen
import com.itec.yussarent.util.NetworkError
import com.itec.yussarent.viewModels.RoomViewModel

@Composable
fun HomeScreen(
    data: Map<String, Int>,
    navController: NavHostController,
    roomViewModel : RoomViewModel = hiltViewModel(),
    buildings : List<Building>
) {
    val networkErrorDialog by NetworkError.networkError.collectAsState()

    if (networkErrorDialog!=null) {
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
                        data["total"]?.let { AllRoomsScreen(it, navController) }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Screen.AvailableRooms.route)
                            }
                    ) {
                        data["available"]?.let {
                            AvailableRoomsScreen(
                                it,
                                navController = navController,
                                buildings = buildings
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
                        data["occupied"]?.let {
                            OccupiedRoomsScreen(
                                it,
                                navController=navController,
                                buildings = buildings
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
                        data["payments"]?.let {
                            PaymentScreen(it, navController)
                        }
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
                        data["invoices"]?.let {
                            InvoiceScreen(it, navController)
                        }
                    }
                }

            }
        }
}
