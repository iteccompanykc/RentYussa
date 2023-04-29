package com.example.yussarent.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.yussarent.data.models.Building
import com.example.yussarent.data.models.Room
import com.example.yussarent.data.models.Screen
import com.example.yussarent.viewModels.RoomViewModel

@Composable
fun OccupiedRoomsScreen(rooms:List<Room>?= emptyList(), roomViewModel: RoomViewModel?=null, navController: NavHostController, isHome:Boolean=true) {
    val iconColor = MaterialTheme.colorScheme.primary
    val buildingsState = roomViewModel?.buildings?.observeAsState()
    val buildings: List<Building>? = buildingsState?.value
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
                buildings?.let { buildings1 ->
                    CompanyBuildingList(buildings = buildings1) {selectedBuilding->
                        println("Selected $selectedBuilding")
                        if (selectedBuilding != null) {
                            buildingRooms = if (selectedBuilding.id!=0) {
                                roomViewModel.getBuildingOccupiedRooms(selectedBuilding.id.toString())
                                roomViewModel.buildingOccupiedRooms.value?: emptyList()
                            } else{
                                rooms?: emptyList()
                            }
                        }
                    }
                }
            }
            buildingRooms?.size?.let {
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


