package com.example.storysappa

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.storysappa.DetailActivity.Companion.EXTRA_STORY
import com.example.storysappa.databinding.AddStoryActivityBinding
import com.example.storysappa.databinding.DetailStoryBinding
import com.example.storysappa.main.MainActivity
import com.example.storysappa.story.ListStoryItem
import com.example.storysappa.story.StoryViewModel
import com.example.storysappa.upload.UploadViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity: AppCompatActivity() {
    private lateinit var binding: AddStoryActivityBinding

    private var currentImageUri: Uri? = null

    private val viewModelUpload by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddStoryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGallery.setOnClickListener { startGallery() }

        binding.buttonCamera.setOnClickListener { startCamera() }

        binding.buttonUpload.setOnClickListener { uploadImage() }

        viewModelUpload.uploadResult.observe(this) { response ->
            if (!response.error) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imagePlaceholder.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.editDescription.text.toString()

//            showLoading(true)

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            lifecycleScope.launch {
//                val pref = UserPreference.getInstance(dataStore)
//                val user = pref.getSession().first()

                viewModelUpload.uploadStory(multipartBody, requestBody)
            }
        } ?: showToast("kosong")
    }

//    private fun showLoading(isLoading: Boolean) {
//        binding.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}