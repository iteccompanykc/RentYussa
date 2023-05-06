package com.itec.yussarent.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itec.yussarent.data.models.User
import com.itec.yussarent.data.repositories.UserRepository
import com.itec.yussarent.util.CompanyState
import com.itec.yussarent.util.Resource
import com.itec.yussarent.util.UserSingleton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(private val userRepository: UserRepository, private val coroutineScope: CoroutineScope) :ViewModel() {
    private val _state = mutableStateOf(CompanyState())
    val state: State<CompanyState> = _state
    init {
        coroutineScope.launch {
            getCompany()
        }
    }
    fun refresh(){
        coroutineScope.launch {
            getCompany()
        }
    }
   private suspend fun getCompany() {
        var user: User? = UserSingleton.user
        user?.let {
            userRepository.getCompany(it.co_id)
        }?.onEach { result->
            when(result){
                is Resource.Success->{
                    _state.value = CompanyState(company = result.data)
                }
                is Resource.Error->{
                    _state.value = CompanyState(error = result.message?: "Un expected error occurred")
                }
                is Resource.Loading->{
                    _state.value = CompanyState(isLoading = true)
                }
            }
        }?.launchIn(viewModelScope)
    }

}
