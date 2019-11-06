package com.app.flikrsearchdemo.data.repository.photos_search.response.photo_detail

import com.google.gson.annotations.SerializedName

/**
 * Created by Your name on 2019-11-06.
 */
data class PhotoDetailResponse(@SerializedName("photo") val photoResult: PhotoDetail) {
}