package com.example.storysappa.upload

import com.example.storysappa.UserPreference
import com.example.storysappa.story.StoryApiService
import com.example.storysappa.story.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadApiRepository(
    private val apiService: UploadApiService,
    private val pref: UserPreference) {

    suspend fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): UploadApiResponse{
        return apiService.uploadImage(file, description)
    }

    companion object {
        @Volatile
        private var INSTANCE: UploadApiRepository? = null

        fun getInstance(apiService: UploadApiService, pref: UserPreference): UploadApiRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UploadApiRepository(apiService, pref).also { INSTANCE = it }
            }
        }
    }
}