package com.example.imagesearchapp.network


import androidx.paging.PagingSource
import androidx.paging.PagingState

import com.example.imagesearchapp.model.UnsplashPhotosResponse.Result
import retrofit2.HttpException
import java.io.IOException

class UnsplashPhotosPagingSource(
    private val services: UnsplashPhotoServices,
    private val query: String
) :
    PagingSource<Int, Result>() {


    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {

        return try {


            val currentPage = params.key ?: FIRST_PAGE_INDEX

            val response = services.getPhotos(query = query, page = currentPage, params.loadSize)

            val photoList = response.results ?: emptyList()

            val prevPage = if (currentPage == FIRST_PAGE_INDEX) null else currentPage.minus(1)
            val nextPage = if (photoList.isEmpty()) null else currentPage + 1

            LoadResult.Page(photoList, prevPage, nextPage)


        } catch (io: IOException) {
            LoadResult.Error(io)
        } catch (http: HttpException) {
            LoadResult.Error(http)
        }

    }

    companion object {
        private const val FIRST_PAGE_INDEX = 1

    }

}