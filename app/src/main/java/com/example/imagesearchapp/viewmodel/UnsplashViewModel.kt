package com.example.imagesearchapp.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.imagesearchapp.network.UnsplashPhotoServices
import com.example.imagesearchapp.repository.UnsplashPhotosRepository


class UnsplashViewModel : ViewModel() {
    private val services = UnsplashPhotoServices.create()
    private val repo = UnsplashPhotosRepository.create(services)
    private val querySearch = MutableLiveData("cat")

    fun searchPhotos(query: String) {
        querySearch.value = query
    }

    val getPhotos =
        querySearch.switchMap {
            repo.getPhoto(it).cachedIn(viewModelScope).asLiveData()
    }

}