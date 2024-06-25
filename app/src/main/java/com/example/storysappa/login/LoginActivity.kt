package com.example.storysappa.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storysappa.UserModel
import com.example.storysappa.UserPreference
import com.example.storysappa.UserRepository
import com.example.storysappa.ViewModelFactory
import com.example.storysappa.dataStore
import com.example.storysappa.databinding.ActivityLoginBinding
import com.example.storysappa.main.MainActivity
import com.example.storysappa.main.MainViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var binding: ActivityLoginBinding

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(applicationContext)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        val userPreference = UserPreference.getInstance(dataStore)
        userRepository = UserRepository.getInstance(userPreference)

        loginViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showLoading(isLoading)
            } else {
                showLoading(isLoading)
            }
        })

        setupView()
        setupAction()
        observeViewModel()

        loginViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            loginViewModel.login(email, password)
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginResponse.observe(this, Observer { response ->
            response?.let {
                if (it.error == false) {
                    showSuccessDialog(it.message ?: "Login successful")
                } else {
                    showErrorDialog(it.message ?: "Login failed")
                }
            }
        })

        loginViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                showErrorDialog(it)
            }
        })
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
            setMessage(errorMessage)
            setPositiveButton("OK") { dialog, _ ->
                finish()
            }
            create()
            show()
        }
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Success")
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                navigateToHome()
            }
            show()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progbar.visibility = View.VISIBLE
        } else {
            binding.progbar.visibility = View.GONE
        }
    }
}