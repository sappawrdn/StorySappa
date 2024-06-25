package com.example.storysappa.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel(),
    ViewModelProvider.Factory {

    private val _stories = MutableLiveData<StoryResponse>()
    val stories: LiveData<StoryResponse> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun fetchStories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = storyRepository.storyRepo()
                _stories.postValue(response)
                _isLoading.value = false
                Log.d("story response", "berhasil")
            }  catch (e: HttpException) {
                Log.e("API Error", e.response()?.errorBody()?.string() ?: "Unknown error")
            } catch (e: Exception) {
                Log.e("API Error", e.message ?: "Unknown error")
            }
        }
    }
}