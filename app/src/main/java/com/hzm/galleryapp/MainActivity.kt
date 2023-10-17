package com.hzm.galleryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hzm.galleryapp.adapter.ImageAdapter
import com.hzm.galleryapp.api.ImageService
import com.hzm.galleryapp.api.RetrofitHelper
import com.hzm.galleryapp.models.Hit
import com.hzm.galleryapp.models.HitWithFavorite
import com.hzm.galleryapp.repository.ImageRepository
import com.hzm.galleryapp.roomDb.FavoriteEntity
import com.hzm.galleryapp.viewModels.MainViewModel
import com.hzm.galleryapp.viewModels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = (application as ImageApplication).imageRepository
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ImageAdapter(emptyList(), { clickedImage ->
            toggleFavorite(clickedImage)
        }, mainViewModel)
        recyclerView.adapter = adapter

        // Create an array of category TextViews
        val categoryViews = arrayOf(
            findViewById<TextView>(R.id.bgCategory),
            findViewById<TextView>(R.id.fashionCategory),
            findViewById<TextView>(R.id.natureCategory),
            findViewById<TextView>(R.id.scienceCategory),
            findViewById<TextView>(R.id.educationCategory),
            findViewById<TextView>(R.id.feelingCategory),
            findViewById<TextView>(R.id.healthCategory),
            findViewById<TextView>(R.id.peopleCategory),
            findViewById<TextView>(R.id.religionCategory)
        )

        // Initialize the first category as selected
//        categoryViews[0].setTextColor(resources.getColor(R.color.black))

        categoryViews.forEach { categoryView ->
            categoryView.setOnClickListener {
                categoryViews.forEach {
                    it.setTextColor(resources.getColor(R.color.light_grey))
                }
                categoryView.setTextColor(resources.getColor(R.color.black))

                val selectedCategory = categoryView.text.toString()
                mainViewModel.loadImagesByCategory(selectedCategory) // Load images based on the selected category
            }
        }

        mainViewModel.images.observe(this, Observer { imageList ->
            val hitsWithFavorites = imageList.hits.map { hit: Hit ->
                val isFavorite = mainViewModel.getFavorites().value?.any { favorite: FavoriteEntity ->
                    favorite.id == hit.id
                } ?: false // Default to false if the list is null
                HitWithFavorite(hit, isFavorite)
            }
            adapter.data = hitsWithFavorites
            adapter.notifyDataSetChanged()
        })

        mainViewModel.favoriteImages.observe(this, Observer { favoriteList ->
            // Handle changes in favorite images here if needed
        })

        mainViewModel.favoriteStatus.observe(this, Observer { favoriteStatus ->
            // Handle changes in favorite status here if needed
        })

        val fabFavorite: FloatingActionButton = findViewById(R.id.fabFavorite)
        fabFavorite.setOnClickListener {
            // Navigate to FavoriteActivity when the FAB is clicked
            val intent = Intent(this@MainActivity, FavoritesActivity::class.java)
            startActivity(intent)
        }

    }

    private fun toggleFavorite(image: Hit) {
        mainViewModel.toggleFavorite(image)
    }
}