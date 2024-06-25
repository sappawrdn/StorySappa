package com.example.storysappa.upload

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storysappa.story.StoryRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val repository: UploadApiRepository): ViewModel() {
    val uploadResult = MutableLiveData<UploadApiResponse>()

    fun uploadStory(file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            try {
                val response = repository.uploadStory(file, description)
                uploadResult.postValue(response)
            } catch (e: Exception) {
                uploadResult.postValue(UploadApiResponse(error = true, message = e.message.toString()))
            }
        }
    }
}