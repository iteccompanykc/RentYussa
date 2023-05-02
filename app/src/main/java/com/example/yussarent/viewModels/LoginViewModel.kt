package com.example.yussarent.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yussarent.data.repositories.impl.UserRepositoryImpl
import com.example.yussarent.util.LoginResult
import com.example.yussarent.util.LoginResultSingleton
import com.example.yussarent.util.UserSingleton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepositoryImpl) :
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
                    LoginResultSingleton.resetLoginResult()
                }
            }.onFailure { exception ->
                _loginResult.value = LoginResult.Error(exception.message ?: "Unknown error")
                LoginResultSingleton.resetLoginResult()
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


