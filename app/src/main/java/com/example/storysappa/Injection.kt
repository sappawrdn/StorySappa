package com.example.storysappa

import android.content.Context
import com.example.storysappa.login.LoginApiConfig
import com.example.storysappa.login.LoginRepository
import com.example.storysappa.signup.SignupApiconfig
import com.example.storysappa.signup.SignupRepository
import com.example.storysappa.story.StoryApiConfig
import com.example.storysappa.story.StoryRepository
import com.example.storysappa.upload.UploadApiConfig
import com.example.storysappa.upload.UploadApiRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideSignupRepository(): SignupRepository {
        val apiService = SignupApiconfig.getApiService()
        return SignupRepository.getInstance(apiService)
    }

    fun provideLoginRepository(): LoginRepository{
        val apiService = LoginApiConfig.getApiService()
        return LoginRepository.getInstance(apiService)
    }

    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = StoryApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, pref)
    }

    fun provideUploadRepository(context: Context): UploadApiRepository{
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = UploadApiConfig.getApiService(user.token)
        return UploadApiRepository.getInstance(apiService, pref)
    }

}