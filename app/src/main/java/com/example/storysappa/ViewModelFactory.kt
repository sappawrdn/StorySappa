package com.example.storysappa

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storysappa.login.LoginRepository
import com.example.storysappa.login.LoginViewModel
import com.example.storysappa.main.MainViewModel
import com.example.storysappa.signup.SignupRepository
import com.example.storysappa.signup.SignupViewModel
import com.example.storysappa.story.StoryRepository
import com.example.storysappa.story.StoryViewModel
import com.example.storysappa.upload.UploadApiRepository
import com.example.storysappa.upload.UploadViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val signupRepository: SignupRepository,
    private val loginRepository: LoginRepository,
    private val storyRepository: StoryRepository,
    private val uploadApiRepository: UploadApiRepository
) : ViewModelProvider.NewInstanceFactory()
{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository,loginRepository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(signupRepository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(uploadApiRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return ViewModelFactory(
                Injection.provideRepository(context),
                Injection.provideSignupRepository(),
                Injection.provideLoginRepository(),
                Injection.provideStoryRepository(context),
                Injection.provideUploadRepository(context)
            )
        }
    }
}