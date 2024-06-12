package com.example.storysappa.signup

import com.example.storysappa.UserPreference
import com.example.storysappa.UserRepository

class SignupRepository private constructor(
    private val signupApiService: SignupApiService
) {
    suspend fun registerRepo(name: String, email: String, password: String): SignupResponse {
        return signupApiService.register(name, email, password)
    }

    companion object {
        @Volatile
        private var INSTANCE: SignupRepository? = null

        fun getInstance(signupApiService: SignupApiService): SignupRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SignupRepository(signupApiService).also { INSTANCE = it }
            }
        }
    }
}