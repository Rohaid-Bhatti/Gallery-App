package com.hzm.galleryapp.models

import com.hzm.galleryapp.roomDb.FavoriteEntity

data class HitWithFavorite(
    val hit: Hit,
    var isFavorite: Boolean = false,
)/*: java.io.Serializable*/
