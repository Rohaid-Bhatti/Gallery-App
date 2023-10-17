package com.hzm.galleryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hzm.galleryapp.adapter.FavoriteAdapter
import com.hzm.galleryapp.roomDb.FavoriteEntity
import com.hzm.galleryapp.roomDb.ImageDatabase
import com.hzm.galleryapp.viewModels.MainViewModel
import com.hzm.galleryapp.viewModels.MainViewModelFactory

class FavoritesActivity : AppCompatActivity() {
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewFavorites)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the repository and ViewModel
        val repository = (application as ImageApplication).imageRepository
        viewModel = ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        // Create the adapter with an empty list
        favoriteAdapter = FavoriteAdapter(
            { clickedFavorite ->
                // Handle item click here if needed
            },
            { removedFavorite ->
                // Handle item removal here
                viewModel.removeFavorite(removedFavorite)
            }
        )

        recyclerView.adapter = favoriteAdapter

        // Observe the list of favorite items using LiveData
        viewModel.favoriteImages.observe(this, Observer { favoriteEntities ->
            // Submit the list of favorite items to the adapter
            favoriteAdapter.submitList(favoriteEntities)
        })

        // Fetch and observe favorite image IDs
        viewModel.favoriteImageIds.observe(this, Observer { favoriteImageIds ->
            // Pass the list of favorite image IDs to the adapter
            favoriteAdapter.setFavoriteImageIds(favoriteImageIds)
        })

        // Fetch favorite image IDs
        viewModel.fetchFavoriteImageIds()
    }
}