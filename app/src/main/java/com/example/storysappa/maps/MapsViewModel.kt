package com.example.storysappa.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storysappa.story.StoryRepository
import com.example.storysappa.story.StoryResponse
import kotlinx.coroutines.launch

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _storiesWithLocation = MutableLiveData<StoryResponse>()
    val storiesWithLocation: LiveData<StoryResponse> = _storiesWithLocation

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            try {
                val response = storyRepository.getStoriesWithLocation()
                Log.d("MapsViewModel", "Fetched stories: ${response.listStory}")
                _storiesWithLocation.value = response
            } catch (e: Exception) {
                _error.value = "Error fetching stories: ${e.message}"
            }
        }
    }
}