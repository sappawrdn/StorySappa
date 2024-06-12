package com.example.storysappa.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.storysappa.UserModel
import com.example.storysappa.UserPreference
import com.example.storysappa.UserRepository
import com.example.storysappa.ViewModelFactory
import com.example.storysappa.dataStore
import com.example.storysappa.databinding.ActivityLoginBinding
import com.example.storysappa.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreference = UserPreference.getInstance(dataStore)
        userRepository = UserRepository.getInstance(userPreference)


        setupView()
        setupAction()
        observeViewModel()
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
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
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
                dialog.dismiss()
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
                finish()
            }
            create()
            show()
        }
    }
}