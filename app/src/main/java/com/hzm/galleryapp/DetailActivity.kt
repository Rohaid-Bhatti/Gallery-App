package com.hzm.galleryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imageView = findViewById<ImageView>(R.id.detailsImageView)
        val likesTextView = findViewById<TextView>(R.id.likesTextView)
        val viewsTextView = findViewById<TextView>(R.id.viewsTextView)
        val commentsTextView = findViewById<TextView>(R.id.commentsTextView)
        val downloadsTextView = findViewById<TextView>(R.id.downloadsTextView)
        val tagsTextView = findViewById<TextView>(R.id.tagsTextView)
        val typesTextView = findViewById<TextView>(R.id.typesTextView)
        val userTextView = findViewById<TextView>(R.id.userTextView)

        // Retrieve data passed from the ImageAdapter
        val imageUrl = intent.getStringExtra("image_url")
        val likes = intent.getIntExtra("likes", 0)
        val views = intent.getIntExtra("views", 0)
        val comments = intent.getIntExtra("comments", 0)
        val downloads = intent.getIntExtra("downloads", 0)
        val tags = intent.getStringExtra("tags")
        val type = intent.getStringExtra("type")
        val user = intent.getStringExtra("user")

        // Use Glide to load and display the image
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.photo)
            .into(imageView)

        // Set text for other UI components
        likesTextView.text = "Likes: $likes"
        viewsTextView.text = "Views: $views"
        commentsTextView.text = "Comments: $comments"
        downloadsTextView.text = downloads.toString()
        tagsTextView.text = tags
        typesTextView.text = type
        userTextView.text = user
    }
}