package com.itec.yussarent.ui.views.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
fun OccupiedRoomsScreen(
    rooms: Int? = 0,
    roomViewModel: RoomViewModel = hiltViewModel(),
    navController: NavHostController,
    isHome: Boolean = true,
    buildings: List<Building>
) {
    val iconColor = MaterialTheme.colorScheme.primary
    var buildingRooms by remember {
        mutableStateOf(rooms)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {

            if (!isHome) {
                CompanyBuildingList(buildings =buildings) { selectedBuilding->

                    if (selectedBuilding != null) {
                        buildingRooms = if (selectedBuilding.id!=0) {
                            roomViewModel.getBuildingOccupiedRooms(selectedBuilding.id.toString())
                            roomViewModel.buildingOccupiedRooms.value.size
                        } else{
                            rooms
                        }
                    }
                }
            }
            buildingRooms?.let {
                CardInfoView(
                    it,
                    "Occupied",
                    Screen.OccupiedRooms.route,
                    navController,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Hotel icon",
                            tint = iconColor,
                            modifier = Modifier.size(48.dp)
                        )
                    })
            }
        }
    }
}


