package com.hzm.galleryapp.roomDb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_table")
    fun getFavoritesLiveData(): LiveData<List<FavoriteEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_table WHERE id = :id LIMIT 1)")
    suspend fun isFavorite(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    // Add the new function to get favorite image IDs
    @Query("SELECT imageId FROM favorite_table")
    suspend fun getFavoriteImageIds(): List<Int>

    // Add another function to get favorite image IDs as LiveData
    @Query("SELECT imageId FROM favorite_table")
    fun getFavoriteImageIdsLiveData(): LiveData<List<Int>>

    @Delete
    suspend fun removeFavorite(favorite: FavoriteEntity)

    // Function to remove a favorite by its imageId
    @Query("DELETE FROM favorite_table WHERE id = :id")
    suspend fun removeFavoriteByImageId(id: Int)
}