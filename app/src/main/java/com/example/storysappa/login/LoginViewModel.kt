package com.example.storysappa.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storysappa.UserModel
import com.example.storysappa.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(email: String, password: String){
        viewModelScope.launch {
            try {
                val response = loginRepository.loginRepo(email, password)
                if (response.error == true) {
                    _errorMessage.value = response.message
                } else {
                    _loginResponse.value = response
                    _successMessage.value = response.message
                    response.loginResult?.let {
                        val userModel = UserModel(
                            email = email,
                            token = it.token ?: "",
                            isLogin = true
                        )
                        userRepository.saveSession(userModel)
                    }
                }
            }catch (e: Exception){
                _error.value = "Login failed: ${e.message}"
            }
        }
    }

//    fun saveSession(user: UserModel) {
//        viewModelScope.launch {
//            userRepository.saveSession(user)
//        }
//    }
}