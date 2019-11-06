package com.app.flikrsearchdemo.data.repository.photos_search

import android.util.Log
import com.app.flikrsearchdemo.Constants
import com.app.flikrsearchdemo.data.repository.FlikrApi
import com.app.flikrsearchdemo.data.repository.photos_search.response.SearchResultResponse
import io.reactivex.Single
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Your name on 2019-11-05.
 */

@Singleton
class PhotoSearchRepositoryImpl @Inject constructor(private val imageApi: FlikrApi): PhotoSearchRepository {

    private val TAG = PhotoSearchRepositoryImpl::class.java.simpleName

    override fun queryImage(
        perPage: Int,
        page: Int,
         query: String
    ): Single<SearchResultResponse> {
        return imageApi.searchImages(
            query,
            page)
    }
}