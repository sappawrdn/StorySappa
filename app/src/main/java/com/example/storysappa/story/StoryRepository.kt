package com.example.storysappa.story

import com.example.storysappa.UserPreference

class StoryRepository private constructor(
    private val storyApiService: StoryApiService,
    private val pref: UserPreference
) {
    suspend fun storyRepo(): StoryResponse {
        return storyApiService.getStories()
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(storyApiService: StoryApiService, pref: UserPreference): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: StoryRepository(storyApiService, pref).also { INSTANCE = it }
            }
        }
    }
}