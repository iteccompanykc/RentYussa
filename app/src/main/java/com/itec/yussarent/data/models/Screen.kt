package com.itec.yussarent.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? =null,
    val count: String? = null
) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object AvailableRooms : Screen("available_rooms", "Available", Icons.Filled.Warning, "20")
    object OccupiedRooms : Screen("occupied_rooms", "Occupied", Icons.Filled.Favorite, "300")
    object Invoice : Screen("invoice", "Invoice", Icons.Filled.Info, "23")
    object Payments : Screen("payments", "Payments", Icons.Filled.ThumbUp, "5")
    object Login :Screen("login","")
    object Splash: Screen("splash","")

}

