package com.example.yussarent.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.yussarent.data.models.Building

@Composable
fun BuildingsList(buildings: List<Building>, onBuildingSelected: (Building) -> Unit, selectedBuilding: Building? = null) {
    val emptyBuilding = Building(id = 0, name = "All", address = "")

    LazyRow(modifier = Modifier.padding().background(MaterialTheme.colorScheme.secondaryContainer)) {
        item {
            BuildingCard(building = emptyBuilding, isSelected = emptyBuilding == (selectedBuilding ?: emptyBuilding)) { onBuildingSelected(emptyBuilding) }
        }
        items(buildings) { building ->
            BuildingCard(building = building, isSelected = building == (selectedBuilding ?: emptyBuilding)) { onBuildingSelected(building) }
        }
    }
}




