package com.example.storysappa.story

import com.example.storysappa.login.LoginApiService
import com.example.storysappa.login.LoginRepository
import com.example.storysappa.login.LoginResponse

class StoryRepository private constructor(
    private val storyApiService: StoryApiService
) {
    suspend fun storyRepo(token: String): StoryResponse {
        return storyApiService.getStories(token)
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(storyApiService: StoryApiService): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: StoryRepository(storyApiService).also { INSTANCE = it }
            }
        }
    }
}