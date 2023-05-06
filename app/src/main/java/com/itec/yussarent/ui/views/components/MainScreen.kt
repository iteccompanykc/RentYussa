package com.itec.yussarent.ui.views.components
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itec.yussarent.data.models.*

import com.itec.yussarent.ui.theme.RentTheme
import com.itec.yussarent.util.*
import com.itec.yussarent.viewModels.LoginViewModel
import com.itec.yussarent.viewModels.RoomViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter.*
import java.util.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.itec.yussarent.ui.views.MainActivity
import com.itec.yussarent.util.ThemeManager
import com.itec.yussarent.util.UserSingleton
import com.itec.yussarent.viewModels.CompanyViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    screens: List<Screen>,
    loginViewModel: LoginViewModel= hiltViewModel(),
    roomViewModel: RoomViewModel = hiltViewModel(),
    companyViewModel: CompanyViewModel = hiltViewModel()
) {

    val user: User? = UserSingleton.user
    var loggedOut by remember {
        mutableStateOf(false)
    }
    if (user == null) {
        return
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val isLoading by roomViewModel.overallLoadingState.collectAsState()
    val availableRooms by roomViewModel.availableRooms.collectAsState()
    val occupiedRooms by roomViewModel.occupiedRooms.collectAsState()
    val invoices by roomViewModel.dueInvoices.collectAsState()
    val payments by roomViewModel.duePayments.collectAsState()
    val totalRooms by roomViewModel.rooms.collectAsState()
    val buildings by roomViewModel.buildings.collectAsState()
    val companyState = companyViewModel.state.value


    val roomCounts = mapOf(
        Screen.AvailableRooms to availableRooms.size,
        Screen.OccupiedRooms to occupiedRooms.size,
        Screen.Invoice to invoices.size,
        Screen.Payments to payments.size,
        Screen.Home to 0
    )
    val homeData= mapOf(
       "total" to totalRooms.size,
       "available" to availableRooms.size,
       "invoices" to invoices.size,
        "payments" to payments.size,
        "occupied" to occupiedRooms.size,

    )
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    suspend fun DrawerState.openOrClose() {
        if (isClosed) open() else close()
    }
    if (loggedOut) {
        val intent = Intent(LocalContext.current, MainActivity::class.java)
        LocalContext.current.startActivity(intent)
        loginViewModel.logout()
        (LocalContext.current as Activity).finishAffinity()
    }

    // Set up the theme for the whole app
    val navController = rememberNavController()
    RentTheme(darkTheme = ThemeManager.isDarkTheme) {

        TopNavigationDrawer(drawerState, content = {

            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(
                                onClick = { scope.launch { drawerState.openOrClose() } },
                                modifier = Modifier.padding(16.dp)
                            ) {
                                val icon =
                                    if (drawerState.isClosed) Icons.Default.Menu else Icons.Default.Close
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        },
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.padding(12.dp))
                                if (companyState.isLoading){
                                    LoadingAnimation3()
                                }
                                companyState.company.let {company->
                                    if (company != null) {
                                        company.cmp_full?.let { Text(text = it) }
                                    }
                                }
                                if(companyState.error.isNotBlank()){
                                    InfoDialog(
                                        title = "Whoops!",
                                        desc = companyState.error,
                                        onRetry = {
                                          companyViewModel.refresh()
                                        },
                                        onCancel = {
                                            companyViewModel.refresh()
                                        }
                                    )
                                }

                            }
                        },

                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                3.dp
                            )
                        ),
                        scrollBehavior = scrollBehavior

                    )
                },
                bottomBar = {
                    BottomNavigationBar(
                        navController = navController,
                        screens = screens,
                        roomCounts = roomCounts
                    )
                }
            ) { padding ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {

                        composable(Screen.Home.route) {
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
                                    if (isLoading) {
                                        Box(
                                            modifier = Modifier.size(128.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            LoadingAnimation3()
                                            // DialogBoxLoading()
                                        }
                                    } else {
                                        HomeScreen(
                                            data = homeData,
                                            navController,
                                            buildings=buildings
                                        )
                                    }
                                }
                            }
                        }
                        composable(Screen.AvailableRooms.route) {
                            AvailableRoomsScreen(
                                rooms = availableRooms.size,
                                navController,
                                isHome = false,
                                buildings=buildings
                            )
                        }
                        composable(Screen.OccupiedRooms.route) {
                            OccupiedRoomsScreen(
                                rooms = occupiedRooms.size,
                                navController=navController,
                                isHome = false,
                                buildings=buildings
                            )
                        }
                        composable(Screen.Invoice.route) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {

                                ShowInvoicesWithDate(navController=navController,buildings=buildings)

                            }


                        }
                        composable(Screen.Payments.route) {

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isLoading) {
                                    Box(
                                        modifier = Modifier.size(128.dp),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        DialogBoxLoading()
                                    }
                                } else {
                                    ShowPaymentsWithDate(navController=navController,buildings=buildings)
                                }
                            }

                        }
                        composable("login") {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LoginScreen(loginViewModel, navController = navController)
                            }
                        }
                    }
                }

            }

        }, onLogout = {
            navController.navigate("login")
            loggedOut = true

        })

    }
}

@Composable
fun AllRoomsScreen(roomsNumber: Int, navController: NavHostController) {
    val iconColor = MaterialTheme.colorScheme.primary
    CardInfoView(roomsNumber, "Total", Screen.Home.route, navController, icon = {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Hotel icon",
            tint = iconColor,
            modifier = Modifier.size(48.dp)
        )
    }
    )
}
@Composable
fun CompanyBuildingList(
    buildings: List<Building>,
    onBuildingSelect: (building: Building?) -> Unit
) {
    var selectedBuilding: Building? by remember { mutableStateOf(null) }
    Column {
        BuildingsList(
            buildings = buildings, onBuildingSelected = { building ->
                selectedBuilding = building
            },
            selectedBuilding = selectedBuilding
        )
        selectedBuilding?.let { onBuildingSelect.invoke(it) }
    }

}



