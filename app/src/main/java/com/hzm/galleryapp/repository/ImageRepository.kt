package com.hzm.galleryapp.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hzm.galleryapp.api.ImageService
import com.hzm.galleryapp.models.Hit
import com.hzm.galleryapp.models.ImageList
import com.hzm.galleryapp.roomDb.FavoriteEntity
import androidx.lifecycle.viewModelScope
import com.hzm.galleryapp.roomDb.ImageDatabase
import com.hzm.galleryapp.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageRepository(
    private val imageService: ImageService,
    private val imageDatabase: ImageDatabase,
    private val applicationContext: Context
) {
    private val imagesLiveData = MutableLiveData<ImageList>()
    private val favoriteStatusLiveData = MutableLiveData<Boolean>()

    // Change favoriteImageIdsLiveData to MutableLiveData
    val favoriteImageIdsLiveData: MutableLiveData<List<Int>> =
        MutableLiveData()

    val images: LiveData<ImageList>
        get() = imagesLiveData
    val favoriteStatus: LiveData<Boolean>
        get() = favoriteStatusLiveData

    // Return LiveData here
    fun getFavorites(): LiveData<List<FavoriteEntity>> {
        return imageDatabase.favoriteDao().getFavoritesLiveData()
    }

    //here i make changes
    suspend fun getImages(page: Int, category: String) {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = imageService.getImages(category, page)

            if (result?.body() != null) {
                imageDatabase.imageDao().addImages(result.body()!!.hits)
                imagesLiveData.postValue(result.body())
            }
        } else {
            val images = imageDatabase.imageDao().getImages()
            val imageList = ImageList(images, 1, 1)
            imagesLiveData.postValue(imageList)
        }
    }

    // Suspend function to check if an image is a favorite
    suspend fun isFavorite(imageId: Int): Boolean {
        return imageDatabase.favoriteDao().isFavorite(imageId)
    }

    // Suspend function to get favorite image IDs
    suspend fun getFavoriteImageIds(): List<Int> {
        return imageDatabase.favoriteDao().getFavoriteImageIds()
    }

    suspend fun fetchFavoriteImageIds() {
        val favoriteImageIds = imageDatabase.favoriteDao().getFavoriteImageIds()
        favoriteImageIdsLiveData.value = favoriteImageIds
    }

    // Function to remove a favorite by its imageId
    suspend fun removeFavorite(favorite: FavoriteEntity) {
        withContext(Dispatchers.IO) {
            imageDatabase.favoriteDao().removeFavorite(favorite)
        }
    }

    suspend fun toggleFavorite(image: Hit) {
        withContext(Dispatchers.IO) {
            val isFavorite = imageDatabase.favoriteDao().isFavorite(image.id)
            Log.d("MyME", isFavorite.toString())

            if (isFavorite) {
                // Image is a favorite, remove it
                imageDatabase.favoriteDao().removeFavoriteByImageId(image.id)
            } else {
                // Image is not a favorite, add it
                val favoriteEntity = FavoriteEntity(
                    0,  // You can use 0 here as the auto-generated ID
                    imageId = image.imageId,
                    largeImageURL = image.largeImageURL,
                    id = image.id,
                    likes = image.likes,
                    views = image.views,
                    tags = image.tags,
                    type = image.type,
                    downloads = image.downloads,
                    comments = image.comments,
                    user = image.user,
                )
                imageDatabase.favoriteDao().addFavorite(favoriteEntity)
            }

            // Update the favorite status LiveData if needed
            // favoriteStatusLiveData.postValue(!isFavorite)
        }
    }
}