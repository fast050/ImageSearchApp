package com.example.imagesearchapp.network

import com.example.imagesearchapp.model.UnsplashPhotosResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashPhotoServices {


    // @Headers("Accept-Version: v1","Authorization: Client-ID YOUR_ACCESS_KEY")
    @Headers(
        "Accept-Version: v1",
        "Authorization: Client-ID LEqrWuWMfXGKtHeck39hy5xTuVEdrT6ADA1wbDjyC30"
    )
    @GET("search/photos")
    suspend fun getPhotos(
        @Query("query") query: String = "cat",
        @Query("page") page: Int,
        @Query("per_page") per_page:Int
    ): UnsplashPhotosResponse

    companion object {
        private const val baseUrl = "https://api.unsplash.com/"

        fun create() = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(UnsplashPhotoServices::class.java)
    }

}