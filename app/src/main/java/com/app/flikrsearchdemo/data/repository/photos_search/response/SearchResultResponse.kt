package com.app.flikrsearchdemo.data.repository.photos_search.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Your name on 2019-11-05.
 */


data class SearchResultResponse(@SerializedName("photos") val photoResult: PhotoResult,
                                @SerializedName("stat") val statusMessage: String,
                                @SerializedName("message") val errorMessage: String)