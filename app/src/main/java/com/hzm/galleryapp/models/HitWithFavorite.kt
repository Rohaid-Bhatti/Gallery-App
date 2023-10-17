package com.hzm.galleryapp.models

data class HitWithFavorite(
    val hit: Hit,
    var isFavorite: Boolean = false
)
