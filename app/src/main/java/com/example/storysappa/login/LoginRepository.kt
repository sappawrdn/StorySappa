package com.example.storysappa.login

import com.example.storysappa.signup.SignupApiService
import com.example.storysappa.signup.SignupRepository

class LoginRepository private constructor(
    private val loginApiService: LoginApiService
) {
    suspend fun loginRepo(email: String, password: String): LoginResponse {
        return loginApiService.login(email, password)
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginRepository? = null

        fun getInstance(loginApiService: LoginApiService): LoginRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LoginRepository(loginApiService).also { INSTANCE = it }
            }
        }
    }
}