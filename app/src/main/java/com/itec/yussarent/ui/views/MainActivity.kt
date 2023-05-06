package com.itec.yussarent.ui.views

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itec.yussarent.data.models.Screen
import com.itec.yussarent.ui.theme.RentTheme
import com.itec.yussarent.ui.views.components.LoginScreen
import com.itec.yussarent.ui.views.components.MainScreen
import com.itec.yussarent.ui.views.components.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity:  ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RentTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.Splash.route) {
                    composable(Screen.Splash.route) {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            SplashScreen {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            }
                        }
                    }
                    composable(Screen.Login.route) {
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInHorizontally(initialOffsetX = { -it }),
                            exit = slideOutHorizontally(targetOffsetX = { -it })
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LoginScreen(navController = navController)
                            }
                        }
                    }
                    composable(Screen.Home.route) {
                        // Your main screen composable that uses the user object
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInHorizontally(initialOffsetX = { it }),
                            exit = slideOutHorizontally(targetOffsetX = { it })
                        ) {
                            val screens = listOf(
                                Screen.Home,
                                Screen.AvailableRooms,
                                Screen.OccupiedRooms,
                                Screen.Invoice,
                                Screen.Payments
                            )
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                MainScreen(screens = screens)
                            }
                        }
                    }
                }
            }
        }
    }
}





