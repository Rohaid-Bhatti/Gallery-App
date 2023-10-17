package com.hzm.galleryapp.roomDb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hzm.galleryapp.models.Hit

@Dao
interface ImageDao {

    @Insert
    suspend fun addImages(images: List<Hit>)

    @Query("SELECT * FROM image_table")
    suspend fun getImages() : List<Hit>
}