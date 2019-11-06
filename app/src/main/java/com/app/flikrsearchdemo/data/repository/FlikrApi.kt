package com.app.flikrsearchdemo.data.repository

import com.app.flikrsearchdemo.Constants
import com.app.flikrsearchdemo.data.repository.photos_search.response.SearchResultResponse
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

    @GET("services/rest")
    fun searchImages(@Query("method") method: String,
                     @Query("tags") searchTags: String,
                     @Query("page") page: Int,
                     @Query("per_page") pageLimit: Int = 25,
                     @Query("api_key") apiKey: String = Constants.FLIKR_API_KEY,
                     @Query("format") format: String = "json",
                     @Query("nojsoncallback") jsonCallackStatus: Int = 1): Single<SearchResultResponse>

}