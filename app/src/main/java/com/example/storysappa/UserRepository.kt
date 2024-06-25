package com.example.storysappa

import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @JvmStatic
        fun getInstance(userPreference: UserPreference): UserRepository {
            return UserRepository(userPreference)
        }
    }
}