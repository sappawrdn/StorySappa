package com.example.storysappa.story

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StoryApiService {
    @GET("stories")
    suspend fun getStories(): StoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): StoryResponse

    @GET("stories")
    suspend fun getStories2(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoryResponse
}