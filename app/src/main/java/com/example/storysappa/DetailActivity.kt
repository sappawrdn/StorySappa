package com.example.storysappa

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.storysappa.databinding.ActivityMainBinding
import com.example.storysappa.databinding.DetailStoryBinding
import com.example.storysappa.story.ListStoryItem
import com.example.storysappa.welcome.WelcomeActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: DetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story: ListStoryItem? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_STORY, ListStoryItem::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_STORY)
        }

        story?.let {
            bindData(it)
        }


    }

    private fun bindData(story: ListStoryItem) {
        Glide.with(binding.root)
            .load(story.photoUrl)
            .into(binding.imageDetail)

        binding.titleDetail.text = story.name
        binding.descDetail.text = story.description
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}