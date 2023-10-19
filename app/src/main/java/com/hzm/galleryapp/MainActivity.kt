package com.hzm.galleryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hzm.galleryapp.adapter.ImageAdapter
import com.hzm.galleryapp.api.ImageService
import com.hzm.galleryapp.api.RetrofitHelper
import com.hzm.galleryapp.databinding.ActivityMainBinding
import com.hzm.galleryapp.models.Hit
import com.hzm.galleryapp.models.HitWithFavorite
import com.hzm.galleryapp.repository.ImageRepository
import com.hzm.galleryapp.roomDb.FavoriteEntity
import com.hzm.galleryapp.viewModels.MainViewModel
import com.hzm.galleryapp.viewModels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    // Add the ShimmerFrameLayout and RecyclerView variables
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = (application as ImageApplication).imageRepository
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        // Initialize the ShimmerFrameLayout and RecyclerView
        shimmerLayout = findViewById(R.id.shimmerLayout)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ImageAdapter(emptyList(), { clickedImage ->
            toggleFavorite(clickedImage)
        }, mainViewModel)
        recyclerView.adapter = adapter

        // Create an array of category TextViews
        val categoryViews = arrayOf(
            binding.bgCategory,
            binding.fashionCategory,
            binding.natureCategory,
            binding.scienceCategory,
            binding.educationCategory,
            binding.feelingCategory,
            binding.healthCategory,
            binding.peopleCategory,
            binding.religionCategory
        )

        // Set the first category as selected by default
        categoryViews[0].setBackgroundResource(R.drawable.bg_black_textview)
        categoryViews[0].setTextColor(resources.getColor(R.color.white))

        mainViewModel.loadImagesByCategory("backgrounds")

        categoryViews.forEach { categoryView ->
            categoryView.setOnClickListener {
                categoryViews.forEach {
//                    it.setTextColor(resources.getColor(R.color.light_grey))
                    it.setBackgroundResource(R.drawable.bg_textview)
                    it.setTextColor(resources.getColor(R.color.black))
                }
                // Set the background color of the selected category to black
                categoryView.setBackgroundResource(R.drawable.bg_black_textview)

                // Set the text color to white
                categoryView.setTextColor(resources.getColor(R.color.white))
//                categoryView.setTextColor(resources.getColor(R.color.black))

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

            // Show or hide the shimmer effect based on my data
            if (hitsWithFavorites.isEmpty()) {
                recyclerView.visibility = View.INVISIBLE
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmer()
            } else {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE
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