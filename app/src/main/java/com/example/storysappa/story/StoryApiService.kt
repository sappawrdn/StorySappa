package com.example.storysappa.story

import retrofit2.http.GET
import retrofit2.http.Header

interface StoryApiService {
    @GET("stories")
    suspend fun getStories(): StoryResponse
}