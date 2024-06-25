package com.example.storysappa.upload

import com.google.gson.annotations.SerializedName

data class UploadApiResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)
