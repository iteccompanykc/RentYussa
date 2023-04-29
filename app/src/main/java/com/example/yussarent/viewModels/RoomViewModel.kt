package com.example.yussarent.viewModels

import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yussarent.data.models.*
import com.example.yussarent.data.repositories.impl.RentalRepositoryImpl
import com.example.yussarent.util.UserSingleton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val repository: RentalRepositoryImpl,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    private val _rooms: MutableLiveData<List<Room>> = MutableLiveData()
    val rooms: LiveData<List<Room>>
        get() = _rooms
    private val _availableRooms: MutableLiveData<List<Room>> = MutableLiveData()
    val availableRooms: LiveData<List<Room>>
        get() = _availableRooms
    private val _occupiedRooms: MutableLiveData<List<Room>> = MutableLiveData()
    val occupiedRooms: LiveData<List<Room>>
        get() = _occupiedRooms

    private val _buildingAvailableRooms: MutableLiveData<List<Room>> = MutableLiveData()
    val buildingAvailableRooms: LiveData<List<Room>>
        get() = _buildingAvailableRooms
    private val _buildingOccupiedRooms: MutableLiveData<List<Room>> = MutableLiveData()
    val buildingOccupiedRooms: LiveData<List<Room>>
        get() = _buildingOccupiedRooms


    private val _dueInvoices: MutableLiveData<List<Invoice>> = MutableLiveData()
    val dueInvoices: LiveData<List<Invoice>>
        get() = _dueInvoices

   private val _duePayments: MutableLiveData<List<Payment>> = MutableLiveData()
    val duePayments : LiveData<List<Payment>>
    get() = _duePayments
   private val _dueBuildingPayments: MutableLiveData<List<Payment>> = MutableLiveData()
    val dueBuildingPayment : LiveData<List<Payment>>
    get() = _dueBuildingPayments
    private val _buildingDueInvoices:MutableLiveData<List<Invoice>> = MutableLiveData()

    val buildingDueInvoices:LiveData<List<Invoice>>
    get() = _buildingDueInvoices

    private val _buildings:MutableLiveData<List<Building>> = MutableLiveData()
    val buildings:LiveData<List<Building>>
    get() = _buildings

    var user: User? = UserSingleton.user


    fun getRooms() {
        coroutineScope.launch {

            val result = kotlin.runCatching {
                user?.let { repository.getRooms(it.co_id) }
            }
            result.onSuccess { rooms ->
                _rooms.postValue(rooms)
            }.onFailure {
                it.printStackTrace()
            }

        }
    }
    fun getAvailableRooms() {
        coroutineScope.launch {
            val result = kotlin.runCatching {
                user?.let { repository.getAvailableRooms(it.co_id) }
            }

            result.onSuccess { rooms ->
                _availableRooms.postValue(rooms)
            }.onFailure {
                it.printStackTrace()
            }

        }
    }
    fun getOccupiedRooms() {
        coroutineScope.launch {
            val result = kotlin.runCatching {
              user?.let { repository.getOccupiedRooms(it.co_id) }
            }
            result.onSuccess { rooms ->
                _occupiedRooms.postValue(rooms)
            }.onFailure {
                it.printStackTrace()
            }

        }
    }

    fun getDueInvoices(date:String) {
        coroutineScope.launch {
            val result = kotlin.runCatching {
                user?.let {
                    repository.getDueInvoices(companyId = it.co_id, date = date)
                }
            }
            result.onSuccess { invoices ->
                _dueInvoices.postValue(invoices)
                _dueInvoices.value?.let { println("Due invoices: ${it.size} at $date") }
            }.onFailure {
                it.printStackTrace()
            }

        }
    }

    fun getBuildingDueInvoices(date:String, buildingId: String) {
        coroutineScope.launch {
            val result = kotlin.runCatching {

            repository.getBuildingDueInvoices(buildingId = buildingId, date = date)

            }
            result.onSuccess { invoices ->
                _buildingDueInvoices.postValue(invoices)
                _dueInvoices.value?.let { println("Due invoices: ${it.size} at $date") }
            }.onFailure {
                it.printStackTrace()
            }

        }
    }


    fun getDuePayments(date:String) {
        coroutineScope.launch {
            val result = kotlin.runCatching {
                user?.let {
                    repository.getDuePayments(it.co_id,date)
                }
            }
            result.onSuccess { payments ->
                _duePayments.postValue(payments)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun getBuildingDuePayments(date:String,buildingId:String) {
        coroutineScope.launch {
            val result = kotlin.runCatching {
                repository.getBuildingDuePayments(buildingId,date)
            }
            result.onSuccess { payments ->

                _dueBuildingPayments.postValue(payments)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
   fun getBuildings(){
       coroutineScope.launch {
           val result = kotlin.runCatching {
               user?.let { user->
                   repository.getBuildings(user.co_id)
               }
           }
           result.onSuccess { buildings ->
               _buildings.postValue(buildings)
               Log.i("ROOM_VIEW_MODEL",buildings.toString())
           }.onFailure {
               it.printStackTrace()
           }
       }

    }
    fun getBuildingAvailableRooms(buildingId: String) {
        coroutineScope.launch {
            val result = kotlin.runCatching {
             repository.getBuildingAvailableRooms(buildingId = buildingId)
            }

            result.onSuccess { rooms ->
                _buildingAvailableRooms.postValue(rooms)
                println("Available: $rooms")
            }.onFailure {
                it.printStackTrace()
            }

        }
    }
    fun getBuildingOccupiedRooms(buildingId: String) {
        coroutineScope.launch {
            val result = kotlin.runCatching {
                repository.getBuildingOccupiedRooms(buildingId = buildingId)
            }
            result.onSuccess { rooms ->
                _buildingOccupiedRooms.postValue(rooms)
                println("Occupied: $rooms")
            }.onFailure {
                it.printStackTrace()
            }

        }
    }
}


