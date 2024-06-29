package com.example.storysappa.signup

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.storysappa.ViewModelFactory
import com.example.storysappa.databinding.ActivitySignupBinding
import com.example.storysappa.login.LoginViewModel
import kotlin.math.sign

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(applicationContext)
        signupViewModel = ViewModelProvider(this, factory).get(SignupViewModel::class.java)

        signupViewModel.isLoading.observe(this, { isLoading ->
            showLoading(isLoading)
        })

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
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            signupViewModel.register(name, email, password)

        }
    }

    private fun observeViewModel() {
        signupViewModel.successMessage.observe(this, Observer { message ->
            if (message != null) {
                showSuccessDialog(message)
            }
        })

        signupViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                showErrorDialog(errorMessage)
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progbar.visibility = View.VISIBLE
        } else {
            binding.progbar.visibility = View.GONE
        }
    }
}