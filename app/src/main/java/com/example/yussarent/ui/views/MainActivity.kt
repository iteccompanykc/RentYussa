package com.example.yussarent.ui.views
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yussarent.data.models.*
import com.example.yussarent.data.repositories.impl.RentalRepositoryImpl
import com.example.yussarent.data.repositories.impl.UserRepositoryImpl
import com.example.yussarent.ui.theme.RentTheme
import com.example.yussarent.util.*
import com.example.yussarent.util.CountServicesSingleton.invoiceSelectedDate
import com.example.yussarent.util.CountServicesSingleton.paymentSelectedDate
import com.example.yussarent.viewModels.LoginViewModel
import com.example.yussarent.viewModels.RoomViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter.*
import java.util.*


class MainActivity : ComponentActivity() {
    lateinit var user: User
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screens = listOf(
            Screen.Home
        )
        val userRepositoryImpl= UserRepositoryImpl(ApiServiceSingleton.createApiService())
        val loginViewModel = LoginViewModel(userRepositoryImpl)
        setContent {
            RentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(screens = screens,loginViewModel)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(screens: List<Screen>, loginViewModel: LoginViewModel) {
//    var isDarkTheme by remember { mutableStateOf(false) }
  val user: User? = UserSingleton.user
    var loggedOut by remember {
        mutableStateOf(false)
    }
    if(user==null){
        return
    }
    val coroutineScope = remember { CoroutineScope(Dispatchers.Main) }
    val rentalRepository = RentalRepositoryImpl(ApiServiceSingleton.createApiService())
    val roomViewModel = RoomViewModel(repository = rentalRepository, coroutineScope = rememberCoroutineScope())
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

   // val roomState by roomViewModel.roomState.collectAsState(RoomState(emptyList(),emptyList(),emptyList(),emptyList(),emptyList()))
    val isLoading by roomViewModel.overallLoadingState.collectAsState()
    var buildings by remember {
        mutableStateOf<List<Building>>(emptyList())
    }
    var rooms by remember {
        mutableStateOf<List<Room>>(emptyList())
    }

    var availableRooms by remember {
        mutableStateOf<List<Room>>(emptyList())
    }
    var occupiedRooms by remember {
        mutableStateOf<List<Room>>(emptyList())
    }
    var invoices by remember {
        mutableStateOf<List<Invoice>>(emptyList())
    }
    var payments by remember {
        mutableStateOf<List<Payment>>(emptyList())
    }
    val availableRoomsScope = rememberCoroutineScope()
    val roomsScope = rememberCoroutineScope()
    val duePaymentsScope = rememberCoroutineScope()
    val occupiedRoomsScope=rememberCoroutineScope()
    val dueInvoicesRoomsScope=rememberCoroutineScope()
    val buildingsScope=rememberCoroutineScope()
    val job = remember { coroutineScope.launch {
        availableRoomsScope.launch {
            roomViewModel.availableRooms.collectLatest { new_rooms ->
                availableRooms = new_rooms
            }
        }
        roomsScope.launch {
            roomViewModel.rooms.collectLatest { new_rooms ->
                rooms = new_rooms
            }
        }

        duePaymentsScope.launch {
            roomViewModel.duePayments.collectLatest { newPayments ->
                payments = newPayments
            }
        }
        occupiedRoomsScope.launch {
            roomViewModel.occupiedRooms.collectLatest { new_rooms ->
                occupiedRooms = new_rooms
            }
        }
        dueInvoicesRoomsScope.launch {
            roomViewModel.dueInvoices.collectLatest { newInvoices ->
                invoices = newInvoices
            }
        }
        buildingsScope.launch {
            roomViewModel.buildings.collectLatest { new_buildings ->
                buildings = new_buildings
            }
        }
    }
    }

    DisposableEffect(Unit) {
        onDispose {
            job.cancel()
        }
    }


    //roomViewModel.collectFlows()

    val roomCounts = mapOf(
        Screen.AvailableRooms to availableRooms.size,
        Screen.OccupiedRooms to occupiedRooms.size,
        Screen.Invoice to invoices.size,
        Screen.Payments to payments.size,
        Screen.Home to 0
    )
    val roomsData= mapOf(
        "Total" to rooms,
        "available" to availableRooms,
       "occupied" to occupiedRooms
    )
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    suspend fun DrawerState.openOrClose() {
        if (isClosed) open() else close()
    }
    if (loggedOut) {
        val intent = Intent(LocalContext.current, LoginActivity::class.java)
        LocalContext.current.startActivity(intent)
        loginViewModel.logout()
        (LocalContext.current as Activity).finishAffinity()
    }

    LaunchedEffect(roomViewModel) {
        roomViewModel.getRooms()
        roomViewModel.getAvailableRooms()
        roomViewModel.getOccupiedRooms()
        roomViewModel.getBuildings()
//        roomViewModel.getDuePayments()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            roomViewModel.getDuePayments(paymentSelectedDate.format(ISO_LOCAL_DATE))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            roomViewModel.getDueInvoices(invoiceSelectedDate.format(ISO_LOCAL_DATE))
        }
    }

    // Set up the theme for the whole app
    val navController = rememberNavController()
    RentTheme(darkTheme = ThemeManager.isDarkTheme) {

            TopNavigationDrawer(drawerState, content = {

                    Scaffold(
                        modifier= Modifier
                            .nestedScroll(scrollBehavior.nestedScrollConnection ),
                        topBar = {
                            TopAppBar(
                                navigationIcon ={
                                    IconButton(
                                        onClick = { scope.launch { drawerState.openOrClose() } },
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        val icon = if (drawerState.isClosed) Icons.Default.Menu else Icons.Default.Close
                                        Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
                                    }
                                },
                                title = {
                                    Row(verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Spacer(modifier =Modifier.padding(12.dp) )
                                        Text(text = "Rent Management")
                                    }
                                },

                                colors = TopAppBarDefaults.mediumTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                                ),
                                scrollBehavior = scrollBehavior
                            )
                        },
                        bottomBar = { BottomNavigationBar(navController = navController, screens = screens, roomCounts = roomCounts) }
                    ) {padding->

                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)) {
                            NavHost(
                                navController = navController,
                                startDestination = Screen.Home.route
                            ) {

                                    composable(Screen.Home.route) {
                                        Surface( color = Color.White,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .wrapContentSize(align = Alignment.Center)
                                                .shadow(if (isLoading) 16.dp else 0.dp)) {
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
                                                        roomsData = roomsData,
                                                        invoices = invoices,
                                                        payments = payments,
                                                        navController
                                                    )
                                                }
                                            }
                                        }

                                    }
                                composable(Screen.AvailableRooms.route) {
                                    AvailableRoomsScreen(rooms = availableRooms, roomViewModel,navController, isHome = false)
                                }
                                composable(Screen.OccupiedRooms.route) {
                                    OccupiedRoomsScreen(rooms=occupiedRooms,roomViewModel,navController,isHome = false)
                                }
                                composable(Screen.Invoice.route) {
                                    ShowInvoicesWithDate(roomViewModel,navController)
                                }
                                composable(Screen.Payments.route) {
                                    ShowPaymentsWithDate(roomViewModel,navController)
                                }
                                composable("login") {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        LoginScreen(loginViewModel, navController = navController)
                                    }
                                }
                            }
                        }

                    }

            }, onLogout ={
                navController.navigate("login")
                loggedOut=true

            })

}
    }


@Composable
fun CompanyBuildingList(buildings: List<Building>, onBuildingSelect: (building:Building?)->Unit){
    var selectedBuilding: Building? by remember { mutableStateOf(null) }
    Column {
        BuildingsList(buildings = buildings, onBuildingSelected = { building ->
            selectedBuilding = building
        },
            selectedBuilding=selectedBuilding
        )
        selectedBuilding?.let { onBuildingSelect.invoke(it) }
    }
}

@Composable
fun AllRoomsScreen(rooms:List<Room>, navController: NavHostController) {
    val iconColor = MaterialTheme.colorScheme.primary
    CardInfoView(rooms.size,"Total", Screen.Home.route,navController, icon = {Icon(
        imageVector = Icons.Filled.MoreVert,
        contentDescription = "Hotel icon",
        tint = iconColor,
        modifier = Modifier.size(48.dp)
    )
    }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val screens = listOf(
        Screen.Home
    )
    val userRepositoryImpl= UserRepositoryImpl(ApiServiceSingleton.createApiService())
    val loginViewModel = LoginViewModel(userRepositoryImpl)
    RentTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen(screens = screens,loginViewModel)
        }
    }
}

