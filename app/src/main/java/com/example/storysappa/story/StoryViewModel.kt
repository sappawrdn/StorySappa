package com.example.storysappa.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel(){

    private val _stories = MutableLiveData<StoryResponse>()
    val stories: LiveData<StoryResponse> = _stories

    fun fetchStories() {
        viewModelScope.launch {
            try {
                val response = storyRepository.storyRepo()
                _stories.postValue(response)
                Log.d("story response", "berhasil")
            }  catch (e: HttpException) {
                Log.e("API Error", e.response()?.errorBody()?.string() ?: "Unknown error")
            } catch (e: Exception) {
                Log.e("API Error", e.message ?: "Unknown error")
            }
        }
    }
}