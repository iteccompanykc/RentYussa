package com.example.yussarent.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.yussarent.data.models.User
import com.example.yussarent.util.UserSingleton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationDrawer(drawerState: DrawerState,content: @Composable () -> Unit, onLogout:  () -> Unit) {
    val items = listOf(Icons.Default.Person)
    val selectedItem = remember { mutableStateOf(items[0]) }
    val user: User? = UserSingleton.user
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (selectedItem.value == Icons.Default.Person && user != null) {
                    ProfileAndSettings(user = user){
                        onLogout()
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        content()
    }


}
