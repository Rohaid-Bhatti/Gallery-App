package com.hzm.galleryapp.models

data class ImageList(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)