package com.example.imagesearchapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.imagesearchapp.model.UnsplashPhotosResponse
import com.example.imagesearchapp.network.UnsplashPhotoServices
import com.example.imagesearchapp.network.UnsplashPhotosPagingSource
import kotlinx.coroutines.flow.Flow

class UnsplashPhotosRepository(private val services: UnsplashPhotoServices) {

    fun getPhoto(query: String)=
        Pager(PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 200),
            pagingSourceFactory = { UnsplashPhotosPagingSource(services = services, query = query) }
        ).flow


    companion object {
        fun create(services: UnsplashPhotoServices): UnsplashPhotosRepository {
            return UnsplashPhotosRepository(services)
        }
    }
}