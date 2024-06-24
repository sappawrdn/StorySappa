package com.example.storysappa.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storysappa.DetailActivity
import com.example.storysappa.R
import com.example.storysappa.UserPreference
import com.example.storysappa.UserRepository
import com.example.storysappa.ViewModelFactory
import com.example.storysappa.dataStore
import com.example.storysappa.databinding.ActivityMainBinding
import com.example.storysappa.story.StoryViewModel
import com.example.storysappa.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: StoryAdapter
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val viewModelStory by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val userRepository: UserRepository by lazy {
        UserRepository.getInstance(UserPreference.getInstance(this.dataStore))
    }


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        viewModelStory.stories.observe(this, { response ->
            adapter.submitList(response.listStory)
        })
        setupView()
        viewModelStory.fetchStories()
        showRecyclerList()
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

//    private fun setupAction() {
//        binding.logoutButton.setOnClickListener {
//            viewModel.logout()
//        }
//    }

    private fun showRecyclerList(){
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        adapter = StoryAdapter{ story ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_STORY, story)
            startActivity(intent)
        }
        binding.rvStory.adapter = adapter
    }

}