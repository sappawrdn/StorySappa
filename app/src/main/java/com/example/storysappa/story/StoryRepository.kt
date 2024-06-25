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
        @JvmStatic
        fun getInstance(storyApiService: StoryApiService, pref: UserPreference): StoryRepository {
            return StoryRepository(storyApiService, pref)
            }
        }
    }