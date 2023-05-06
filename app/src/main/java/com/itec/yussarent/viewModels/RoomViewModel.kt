package com.itec.yussarent.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.itec.yussarent.data.models.*
import com.itec.yussarent.data.repositories.impl.RentalRepositoryImpl
import com.itec.yussarent.data.models.*
import com.itec.yussarent.data.repositories.RentalRepository
import com.itec.yussarent.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val repository: RentalRepository,
    private val coroutineScope: CoroutineScope,
    private val networkErrorHandlerFactory: NetworkErrorHandlerFactory,
    @Named("RoomListNetworkErrorHandler") val roomListNetworkErrorHandler: INetworkErrorHandler,
    @Named("BuildingDueInvoicesNetworkErrorHandler")
     val buildingDueInvoicesNetworkErrorHandler: INetworkErrorHandler,
    @Named("DueInvoicesNetworkErrorHandler")
    val dueInvoicesNetworkErrorHandler: INetworkErrorHandler,
    @Named("AvailableRoomsNetworkErrorHandler")
    val availableRoomsNetworkErrorHandler: INetworkErrorHandler,
    @Named("BuildingDuePaymentNetworkErrorHandler")
     val buildingDuePaymentNetworkErrorHandler: INetworkErrorHandler,
    @Named("BuildingsListNetworkErrorHandler")
    val buildingsListNetworkErrorHandler: INetworkErrorHandler,
    @Named("OccupiedRoomsNetworkErrorHandler")
    val occupiedRoomsNetworkErrorHandler: INetworkErrorHandler
) : ViewModel() {

    private val _duePayementsForBuilding : MutableLiveData<Boolean> = MutableLiveData(false)
    private val _availableRoomsLoading = MutableStateFlow(false)
    val availableRoomsLoading: StateFlow<Boolean> = _availableRoomsLoading

    private val _roomsLoading = MutableStateFlow(false)
    val roomsLoading: StateFlow<Boolean> = _roomsLoading

    private val _occupiedRoomsLoading = MutableStateFlow(false)
    val occupiedRoomsLoading: StateFlow<Boolean> = _occupiedRoomsLoading

    private val _dueInvoicesLoading = MutableStateFlow(false)
    val dueInvoicesLoading: StateFlow<Boolean> = _dueInvoicesLoading

    private val _duePaymentsLoading = MutableStateFlow(false)
    val duePaymentsLoading: StateFlow<Boolean> = _duePaymentsLoading

    private val _buildingsLoading = MutableStateFlow(false)
    val buildingsLoading: StateFlow<Boolean> = _buildingsLoading

    private val _dueBuildingPaymentsLoading = MutableStateFlow(false)
    val dueBuildingPaymentsLoading: StateFlow<Boolean> = _dueBuildingPaymentsLoading

    private val _buildingDueInvoicesLoading = MutableStateFlow(false)
    val buildingDueInvoicesLoading: StateFlow<Boolean> = _buildingDueInvoicesLoading

    private val _buildingsAvailableRoomsLoading = MutableStateFlow(false)
    val buildingsAvailableRoomsLoading: StateFlow<Boolean> = _buildingsAvailableRoomsLoading

    private val _buildingsOccupiedLoading = MutableStateFlow(false)
    val buildingsOccupiedLoading: StateFlow<Boolean> = _buildingsOccupiedLoading

    private val _overallLoadingState = MutableStateFlow(false)

    private val _rooms: MutableStateFlow<List<Room>> = MutableStateFlow(emptyList())
    val rooms: StateFlow<List<Room>>
        get() = _rooms
    private val _availableRooms: MutableStateFlow<List<Room>> = MutableStateFlow(emptyList())
    val availableRooms: StateFlow<List<Room>>
        get() = _availableRooms
    private val _occupiedRooms: MutableStateFlow<List<Room>> = MutableStateFlow(emptyList())
    val occupiedRooms: StateFlow<List<Room>>
        get() = _occupiedRooms

    private val _buildingAvailableRooms: MutableStateFlow<List<Room>> = MutableStateFlow(emptyList())
    val buildingAvailableRooms: StateFlow<List<Room>>
        get() = _buildingAvailableRooms
    private val _buildingOccupiedRooms: MutableStateFlow<List<Room>> = MutableStateFlow(emptyList())
    val buildingOccupiedRooms: StateFlow<List<Room>>
        get() = _buildingOccupiedRooms

    private val _dueInvoices: MutableStateFlow<List<Invoice>> = MutableStateFlow(emptyList())
    val dueInvoices: StateFlow<List<Invoice>>
        get() = _dueInvoices

    private val _duePayments: MutableStateFlow<List<Payment>> = MutableStateFlow(emptyList())
    val duePayments: StateFlow<List<Payment>>
        get() = _duePayments

    private val _dueBuildingPayments: MutableStateFlow<List<Payment>> = MutableStateFlow(emptyList())
    val dueBuildingPayment: StateFlow<List<Payment>>
        get() = _dueBuildingPayments

    private val _buildingDueInvoices: MutableStateFlow<List<Invoice>> = MutableStateFlow(emptyList())
    val buildingDueInvoices: StateFlow<List<Invoice>>
        get() = _buildingDueInvoices

    private val _buildings: MutableStateFlow<List<Building>> = MutableStateFlow(emptyList())
    val buildings: StateFlow<List<Building>>
        get() = _buildings

    var user: User? = UserSingleton.user

    private val loadingFlows = listOf(
        _roomsLoading,
        _availableRoomsLoading,
        _occupiedRoomsLoading,
        _dueInvoicesLoading,
        _duePaymentsLoading,
        _buildingsLoading,
        _dueBuildingPaymentsLoading,
        _buildingDueInvoicesLoading,
        _buildingsAvailableRoomsLoading,
        _buildingsOccupiedLoading
    )
    val roomState: Flow<RoomState> = combine(
        _availableRooms,
        _occupiedRooms,
        _dueInvoices,
        _duePayments,
        _rooms
    ) { availableRooms, occupiedRooms, dueInvoices, duePayments, rooms ->
        RoomState(
            availableRooms = availableRooms,
            occupiedRooms = occupiedRooms,
            dueInvoices = dueInvoices,
            duePayments = duePayments,
            rooms = rooms
        )
    }
    init {
        refresh()
        viewModelScope.launch {
            loadingFlows.all { flow ->
                flow.collect { value ->
                    _overallLoadingState.emit(value)
                }
            }

        }
    }

    val overallLoadingState: StateFlow<Boolean> = _overallLoadingState
    fun refresh(){
        if (roomsLoading.value==false && rooms.value.isEmpty()){
            getRooms()
        }
        if (availableRoomsLoading.value==false && buildingsAvailableRoomsLoading.value==false && availableRooms.value.isEmpty()){
            getAvailableRooms()
        }
        if (occupiedRoomsLoading.value==false && buildingsOccupiedLoading.value==false && occupiedRooms.value.isEmpty()){
            getOccupiedRooms()
        }

        if(dueInvoicesLoading.value==false && dueInvoices.value.isEmpty()) {
            getDueInvoices(
                CountServicesSingleton.invoiceSelectedDate.format(
                    DateTimeFormatter.ISO_LOCAL_DATE
                )
            )
        }
        if (duePaymentsLoading.value==false && dueBuildingPayment.value.isEmpty()) {
            getDuePayments(
                CountServicesSingleton.paymentSelectedDate.format(
                    DateTimeFormatter.ISO_LOCAL_DATE
                )
            )
        }
        if(buildingsLoading.value==false && buildings.value.isEmpty()){
            getBuildings()
        }
    }

    fun getRooms() {
        coroutineScope.launch {
            _roomsLoading.emit(true)
            val result = kotlin.runCatching {
                user?.let { repository.getRooms(it.co_id) }
            }
            result.onSuccess { rooms ->
                if (rooms != null) {
                    _rooms.emit(rooms)
                }
                _roomsLoading.emit(false)

            }.onFailure {

                it.printStackTrace()
                Log.e("ROOM_VIEW_MODEL",it.toString())
                _roomsLoading.emit(false)
                roomListNetworkErrorHandler.handleError(it)
                NetworkError.handleError(it)
            }
        }
    }
    fun getAvailableRooms() {
        coroutineScope.launch {
            _availableRoomsLoading.emit(true)
            val result = kotlin.runCatching {
                user?.let { repository.getAvailableRooms(it.co_id) }
            }
            result.onSuccess { rooms ->
                _availableRooms.emit(rooms!!)
                _availableRoomsLoading.emit(false)
            }.onFailure {

                it.printStackTrace()
                Log.e("ROOM_VIEW_MODEL",it.toString())
                _availableRoomsLoading.emit(false)
                availableRoomsNetworkErrorHandler.handleError(it)
                NetworkError.handleError(it)
            }
        }
    }

    fun getOccupiedRooms() {

        coroutineScope.launch {
            _occupiedRoomsLoading.emit(true)
            val result = kotlin.runCatching {
                user?.let { repository.getOccupiedRooms(it.co_id) }
            }
            result.onSuccess { rooms ->
                _occupiedRooms.emit(rooms!!)
                _occupiedRoomsLoading.emit(false)

            }.onFailure {

                it.printStackTrace()
                Log.e("ROOM_VIEW_MODEL",it.toString())
                _occupiedRoomsLoading.emit(false)
                occupiedRoomsNetworkErrorHandler.handleError(it)
                NetworkError.handleError(it)
            }

        }
    }

    fun getDueInvoices(date:String) {

        coroutineScope.launch {
            _dueInvoicesLoading.emit(true)
            val result = kotlin.runCatching {
                user?.let {
                    repository.getDueInvoices(companyId = it.co_id, date = date)
                }
            }
            result.onSuccess { invoices ->
                _dueInvoices.emit(invoices!!)
                _dueInvoicesLoading.emit(false)
            }.onFailure {

                it.printStackTrace()
                Log.e("ROOM_VIEW_MODEL",it.toString())
                _dueInvoicesLoading.emit(false)
                dueInvoicesNetworkErrorHandler.handleError(it)
                NetworkError.handleError(it)
            }

        }
    }


    fun getBuildingDueInvoices(date:String, buildingId: String) {

        coroutineScope.launch {
            _buildingDueInvoicesLoading.emit(true)
            val result = kotlin.runCatching {
                repository.getBuildingDueInvoices(buildingId = buildingId, date = date)
            }
            result.onSuccess { invoices ->
                _buildingDueInvoices.emit(invoices)
                _buildingDueInvoicesLoading.emit(false)
            }.onFailure {
                it.printStackTrace()
                Log.e("ROOM_VIEW_MODEL",it.toString())
                _buildingDueInvoicesLoading.emit(false)
                buildingDueInvoicesNetworkErrorHandler.handleError(it)
                NetworkError.handleError(it)
            }

        }
    }


    fun getDuePayments(date:String) {

        coroutineScope.launch {
            _duePaymentsLoading.emit(true)
            val result = kotlin.runCatching {
                user?.let {
                    repository.getDuePayments(it.co_id,date)
                }
            }
            result.onSuccess { payments ->
                _duePayments.emit(payments!!)
                _duePaymentsLoading.emit(false)
            }.onFailure {

                it.printStackTrace()
                Log.e("ROOM_VIEW_MODEL PAY",it.toString())
                _duePaymentsLoading.emit(false)
                NetworkError.handleError(it)
            }
        }
    }

    fun getBuildingDuePayments(date:String,buildingId:String) {
        coroutineScope.launch {
            _dueBuildingPaymentsLoading.emit(true)
            val result = kotlin.runCatching {
                repository.getBuildingDuePayments(buildingId,date)
            }
            result.onSuccess { payments ->
                _dueBuildingPayments.emit(payments)
                _dueBuildingPaymentsLoading.emit(false)

            }.onFailure {

                it.printStackTrace()
                Log.e("ROOM_VIEW_MODEL",it.toString())
                _dueBuildingPaymentsLoading.emit(false)
                buildingDuePaymentNetworkErrorHandler.handleError(it)
                NetworkError.handleError(it)
            }
        }
    }

        fun getBuildings(){
       coroutineScope.launch {
           _buildingsLoading.emit(true)
           val result = kotlin.runCatching {
               user?.let { user->
                   repository.getBuildings(user.co_id)
               }
           }
           result.onSuccess {
               _buildings.emit(it!!)
               _buildingsLoading.emit(false)

           }.onFailure {

               it.printStackTrace()
               Log.e("ROOM_VIEW_MODEL",it.toString())
               _buildingsLoading.emit(false)
                buildingsListNetworkErrorHandler.handleError(it)
               NetworkError.handleError(it)
           }
       }

    }
    fun getBuildingAvailableRooms(buildingId: String) {

        coroutineScope.launch {
            _buildingsAvailableRoomsLoading.emit(true)
            val result = kotlin.runCatching {
             repository.getBuildingAvailableRooms(buildingId = buildingId)
            }

            result.onSuccess { rooms ->
                _buildingAvailableRooms.emit(rooms)
                _buildingsAvailableRoomsLoading.emit(false)

            }.onFailure {

                it.printStackTrace()
                _buildingsAvailableRoomsLoading.emit(false)
                Log.e("ROOM_VIEW_MODEL",it.toString())
                availableRoomsNetworkErrorHandler.handleError(it)
                NetworkError.handleError(it)
            }

        }
    }
    fun getBuildingOccupiedRooms(buildingId: String) {
        coroutineScope.launch {
            _buildingsOccupiedLoading.emit(true)
            val result = kotlin.runCatching {
                repository.getBuildingOccupiedRooms(buildingId = buildingId)
            }
            result.onSuccess { rooms ->
                _buildingOccupiedRooms.emit(rooms)
                _buildingsOccupiedLoading.emit(false)

            }.onFailure {

                it.printStackTrace()
                Log.e("ROOM_VIEW_MODEL",it.toString())
                _buildingsOccupiedLoading.emit(false)
                occupiedRoomsNetworkErrorHandler.handleError(it)
                NetworkError.handleError(it)
            }

        }
    }



}


