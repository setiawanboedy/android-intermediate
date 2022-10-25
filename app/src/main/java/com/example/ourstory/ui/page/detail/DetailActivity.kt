package com.example.ourstory.ui.page.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ourstory.MainActivity
import com.example.ourstory.R
import com.example.ourstory.databinding.ActivityDetailBinding
import com.example.ourstory.model.StoryModel
import com.example.ourstory.utils.Constants.STORY
import com.example.ourstory.utils.convertDate
import com.example.ourstory.utils.setImage


class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private var _story: StoryModel? = null
    private val story get() = _story!!

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        _story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(STORY, StoryModel::class.java)
        } else {
            intent.getParcelableExtra<StoryModel>(STORY) as StoryModel
        }

        with(binding) {
            ivPhoto.setImage(story.photoUrl)
            tvCreated.convertDate(story.createdAt, applicationContext)
            tvName.text = story.name
            tvLocation.text = story.lon?.toString() ?: getString(R.string.unknown)
            tvDesc.text = story.description
        }

        playAnimation()
    }

    @SuppressLint("Recycle")
    private fun playAnimation() {

        val photo = ObjectAnimator.ofFloat(binding.ivPhoto, View.SCALE_X, 1f).setDuration(300)
        val created = ObjectAnimator.ofFloat(binding.tvCreated, View.ALPHA, 1f).setDuration(300)
        val name = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(300)
        val location = ObjectAnimator.ofFloat(binding.tvLocation, View.ALPHA, 1f).setDuration(300)
        val ivLocation = ObjectAnimator.ofFloat(binding.ivLocation, View.ALPHA, 1f).setDuration(300)
        val desc = ObjectAnimator.ofFloat(binding.tvDesc, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(location, ivLocation)
        }

        AnimatorSet().apply {
            playSequentially(photo, created, name, together, desc)
            start()
        }
    }

}