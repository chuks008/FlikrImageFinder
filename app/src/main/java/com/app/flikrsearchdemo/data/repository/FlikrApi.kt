package com.app.flikrsearchdemo.data.repository

import com.app.flikrsearchdemo.Constants
import com.app.flikrsearchdemo.data.repository.photos_search.response.SearchResultResponse
import com.app.flikrsearchdemo.data.repository.photos_search.response.photo_detail.PhotoDetailResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Your name on 2019-11-05.
 */
interface FlikrApi {

    @GET(Constants.SEARCH_ENDPOINT)
    fun searchImages(@Query("tags") searchTags: String,
                     @Query("page") page: Int,
                     @Query("method") method: String = Constants.IMAGE_SEARCH_METHOD,
                     @Query("per_page") pageLimit: Int = 25,
                     @Query("api_key") apiKey: String = Constants.FLIKR_API_KEY,
                     @Query("format") format: String = "json",
                     @Query("nojsoncallback") jsonCallackStatus: Int = 1): Single<SearchResultResponse>

    @GET(Constants.SEARCH_ENDPOINT)
    fun getImageDetails(@Query("photo_id") photoId: String,
                        @Query("method") method: String = Constants.IMAGE_DETAIL_METHOD,
                        @Query("api_key") apiKey: String = Constants.FLIKR_API_KEY,
                        @Query("format") format: String = "json",
                        @Query("nojsoncallback") jsonCallackStatus: Int = 1): Single<PhotoDetailResponse>



}