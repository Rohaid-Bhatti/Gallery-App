package com.hzm.galleryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hzm.galleryapp.databinding.ActivityDetailBinding
import com.hzm.galleryapp.models.Hit
import com.hzm.galleryapp.models.HitWithFavorite
import com.hzm.galleryapp.roomDb.FavoriteEntity
import com.hzm.galleryapp.viewModels.MainViewModel
import com.hzm.galleryapp.viewModels.MainViewModelFactory
import kotlin.math.max
import kotlin.math.min

class DetailActivity : AppCompatActivity() {
//    lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val repository = (application as ImageApplication).imageRepository
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)*/

        // Retrieve data passed from the ImageAdapter
        val imageUrl = intent.getStringExtra("image_url")
        val likes = intent.getIntExtra("likes", 0)
        val views = intent.getIntExtra("views", 0)
        val comments = intent.getIntExtra("comments", 0)
        val downloads = intent.getIntExtra("downloads", 0)
        val tags = intent.getStringExtra("tags")
        val type = intent.getStringExtra("type")
        val user = intent.getStringExtra("user")
        /*val id = intent.getIntExtra("id", 0)
        val imageId = intent.getIntExtra("imageId", 0)
        val data = intent.getSerializableExtra("Hit") as HitWithFavorite
        val likes = data.hit.likes
        val views = data.hit.views
        val comments = data.hit.comments
        val downloads = data.hit.downloads
        val tags = data.hit.tags
        val type = data.hit.type
        val user = data.hit.user*/

        // Use Glide to load and display the image
        Glide.with(this)
            .load(imageUrl/*data.hit.largeImageURL*/)
            .placeholder(R.drawable.photo)
            .into(/*imageView*/binding.detailsImageView)

        // Set text for other UI components
        binding.likesTextView.text = "Likes: $likes"
        binding.viewsTextView.text = "Views: $views"
        binding.commentsTextView.text = "Comments: $comments"
        binding.downloadsTextView.text = downloads.toString()
        binding.tagsTextView.text = tags
        binding.typesTextView.text = type
        binding.userTextView.text = user

        // Handle adding to favorites when the heart button is clicked
        /*binding.ibHeart.setOnClickListener {
            mainViewModel.toggleFavorite(data.hit)
            Toast.makeText(this, "Image added to favorites", Toast.LENGTH_SHORT).show()
        }*/
    }
}