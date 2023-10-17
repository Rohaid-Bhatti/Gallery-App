package com.hzm.galleryapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class Hit(
    @PrimaryKey(autoGenerate = true)
    val imageId: Int,
//    val collections: Int,
    val comments: Int,
    val downloads: Int,
    val id: Int,
//    val imageHeight: Int,
//    val imageSize: Int,
//    val imageWidth: Int,
    val largeImageURL: String,
    val likes: Int,
//    val pageURL: String,
//    val previewHeight: Int,
//    val previewURL: String,
//    val previewWidth: Int,
    val tags: String,
    val type: String,
    val user: String,
//    val userImageURL: String,
//    val user_id: Int,
    val views: Int,
//    val webformatHeight: Int,
//    val webformatURL: String,
//    val webformatWidth: Int
)