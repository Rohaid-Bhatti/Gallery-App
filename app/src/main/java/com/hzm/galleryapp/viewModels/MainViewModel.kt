package com.hzm.galleryapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hzm.galleryapp.models.Hit
import com.hzm.galleryapp.models.HitWithFavorite
import com.hzm.galleryapp.models.ImageList
import com.hzm.galleryapp.repository.ImageRepository
import com.hzm.galleryapp.roomDb.FavoriteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ImageRepository) : ViewModel() {

    val images: LiveData<ImageList> = repository.images
    val favoriteStatus: LiveData<Boolean> = repository.favoriteStatus

    // LiveData for observing the list of favorite entities
    val favoriteImages: LiveData<List<FavoriteEntity>> = repository.getFavorites()

    // LiveData for favorite image IDs
    val favoriteImageIds: LiveData<List<Int>> = repository.favoriteImageIdsLiveData

    init {
        // Load images from the default category when the ViewModel is created
        loadImagesByCategory("backgrounds")
    }
    fun loadImagesByCategory(category: String) {
        viewModelScope.launch {
            repository.getImages(1, category)
        }
    }

    // Suspend function to check if an image is a favorite
    suspend fun isFavorite(id: Int): Boolean {
        return repository.isFavorite(id)
    }

    /*fun refreshImages() {
        viewModelScope.launch {
            repository.getImages(1) // You can call this method to refresh the data
        }
    }*/

    // Function to fetch favorite image IDs
    fun fetchFavoriteImageIds() {
        viewModelScope.launch {
            repository.fetchFavoriteImageIds()
        }
    }

    fun removeFavorite(favorite: FavoriteEntity) {
        viewModelScope.launch {
            repository.removeFavorite(favorite)
        }
    }

    fun toggleFavorite(image: Hit) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleFavorite(image)
        }
    }

    fun getFavorites() = repository.getFavorites()
}