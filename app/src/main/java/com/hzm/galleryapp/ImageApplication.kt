package com.hzm.galleryapp

import android.app.Application
import com.hzm.galleryapp.api.ImageService
import com.hzm.galleryapp.api.RetrofitHelper
import com.hzm.galleryapp.repository.ImageRepository
import com.hzm.galleryapp.roomDb.ImageDatabase

class ImageApplication : Application() {

    lateinit var imageRepository: ImageRepository
    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val imageService = RetrofitHelper.getInstance().create(ImageService::class.java)
        val database = ImageDatabase.getDatabase(applicationContext)
        imageRepository = ImageRepository(imageService, database, applicationContext)
    }
}