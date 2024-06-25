package com.example.storysappa.story

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storysappa.UserPreference
import com.example.storysappa.paging.StoryPagingSource

class StoryRepository private constructor(
    private val storyApiService: StoryApiService,
    private val pref: UserPreference
) {
    suspend fun storyRepo(): StoryResponse {
        return storyApiService.getStories()
    }

    suspend fun getStoriesWithLocation(): StoryResponse {
        return storyApiService.getStoriesWithLocation()
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(storyApiService)
            }
        ).liveData
    }

    companion object {
        @JvmStatic
        fun getInstance(storyApiService: StoryApiService, pref: UserPreference): StoryRepository {
            return StoryRepository(storyApiService, pref)
            }
        }
    }