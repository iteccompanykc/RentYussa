package com.itec.yussarent.ui.views.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.itec.yussarent.data.models.Building
import com.itec.yussarent.data.models.Screen
import com.itec.yussarent.viewModels.RoomViewModel

@Composable
fun AvailableRoomsScreen(
    rooms: Int = 0,
    navController: NavHostController,
    isHome: Boolean = true,
    roomViewModel: RoomViewModel = hiltViewModel(),
    buildings: List<Building>
) {
    val iconColor = MaterialTheme.colorScheme.primary
    var buildingRooms by remember {
        mutableStateOf(rooms)
    }
    val networkErrorDialog by roomViewModel.buildingDueInvoicesNetworkErrorHandler.networkError.collectAsState()

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
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            if (!isHome) {
                CompanyBuildingList(buildings = buildings) { selectedBuilding ->
                    if (selectedBuilding != null) {
                        buildingRooms = if (selectedBuilding.id != 0) {
                            roomViewModel.getBuildingAvailableRooms(selectedBuilding.id.toString())
                            roomViewModel.buildingAvailableRooms.value.size
                        } else {
                            rooms
                        }
                    }
                }
            }


                CardInfoView(
                    buildingRooms,
                    "Available",
                    Screen.AvailableRooms.route,
                    navController,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = "Hotel icon",
                            tint = iconColor,
                            modifier = Modifier.size(48.dp)
                        )
                    })
            }
        }
    }

