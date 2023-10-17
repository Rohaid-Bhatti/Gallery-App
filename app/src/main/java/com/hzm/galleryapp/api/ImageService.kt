package com.hzm.galleryapp.api

import com.hzm.galleryapp.models.ImageList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {

    // changes for the per_page
    @GET("?key=39769910-6508c0e9e68e3a718e0981b9a&category=")
    suspend fun getImages(@Query("category") category: String, @Query("page") page: Int): Response<ImageList>
    /*suspend fun getImages(
        @Query("page") page : Int,
//        @Query("per_page") per_page : Int,
    ) : Response<ImageList>*/
}