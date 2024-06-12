package com.example.storysappa.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class SignupViewModel(private val repository: SignupRepository) : ViewModel() {
    private val _registerResponse = MutableLiveData<SignupResponse>()
    val registerResponse: LiveData<SignupResponse> = _registerResponse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: MutableLiveData<String?> = _successMessage

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> = _errorMessage

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.registerRepo(name, email, password)
                val message = response.message
                _successMessage.value = message
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, SignupResponse::class.java)
                val errorMessage = errorBody.message
                _errorMessage.value = errorMessage
            } catch (e: Exception) {
                _errorMessage.value = "Registration failed: ${e.message}"
            }
        }
    }
}