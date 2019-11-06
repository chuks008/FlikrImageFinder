package com.app.flikrsearchdemo.data.repository.photos_search.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Your name on 2019-11-05.
 */
data class ResultPhoto(@SerializedName("id") val photoId: String,
                       @SerializedName("owner") val ownerId: String,
                       @SerializedName("title") val photoTitle: String,
                       @SerializedName("farm") val farmId: Int,
                       @SerializedName("server") val serverId: String,
                       @SerializedName("secret") val secret: String)