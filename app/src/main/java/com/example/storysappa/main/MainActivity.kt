package com.example.storysappa.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storysappa.AddStoryActivity
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

    private lateinit var viewModel: MainViewModel
    private lateinit var storyViewModel: StoryViewModel

    private lateinit var adapter: StoryAdapter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        val factoryStory = ViewModelFactory.getInstance(applicationContext)
        storyViewModel = ViewModelProvider(this, factoryStory).get(StoryViewModel::class.java)

        setSupportActionBar(binding.toolbar)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                storyViewModel.fetchStories()
            }
        }



        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        storyViewModel.stories.observe(this, { response ->
            adapter.submitList(response.listStory)
        })

        setupView()
        storyViewModel.fetchStories()
        showRecyclerList()
        storyViewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                viewModel.logout()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
    }

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

    override fun onResume() {
        super.onResume()
        storyViewModel.fetchStories()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progbarStory.visibility = View.VISIBLE
        } else {
            binding.progbarStory.visibility = View.GONE
        }
    }

}