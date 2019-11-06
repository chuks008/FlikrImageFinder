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
        vararg query: String
    ): Single<SearchResultResponse> {

        var tagKeyWords = ""

        query.forEach {keyword ->
            tagKeyWords += "$keyword,"
        }

        tagKeyWords = tagKeyWords.substring(0, tagKeyWords.length - 1)

        return imageApi.searchImages(
            Constants.IMAGE_SEARCH_METHOD,
            tagKeyWords,
            page)
    }
}