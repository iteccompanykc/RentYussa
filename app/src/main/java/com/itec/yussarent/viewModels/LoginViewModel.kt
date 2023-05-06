package com.itec.yussarent.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itec.yussarent.data.repositories.UserRepository
import com.itec.yussarent.util.LoginResult
import com.itec.yussarent.util.LoginResultSingleton
import com.itec.yussarent.util.NetworkError.handleError
import com.itec.yussarent.util.UserSingleton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: MutableLiveData<LoginResult?>
     get() = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = kotlin.runCatching {
                userRepository.login(username, password)
            }
            result.onSuccess { user ->
                if(user!=null) {
                    _loginResult.value = user.let {
                        LoginResult.Success(it)
                    }
                    UserSingleton.user=user
                    loginResult.value?.let { LoginResultSingleton.setLoginResult(it) }
                }
                else{
                    _loginResult.value = LoginResult.Error("Authentication error")
                }
            }.onFailure { exception ->
                _loginResult.value = LoginResult.Error(exception.message ?: "Unknown error")
                 handleError(exception)
            }
        }
    }
fun logout(){
    _loginResult.value=null
    UserSingleton.user=null
}
    fun resetLoginResult() {
        _loginResult.value=null
    }


}


