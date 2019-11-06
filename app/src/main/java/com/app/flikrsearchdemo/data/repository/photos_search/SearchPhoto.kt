package com.app.flikrsearchdemo.data.repository.photos_search

/**
 * Created by Your name on 2019-11-05.
 */
class SearchPhoto private constructor (
    val photoId: String?,
    val ownerId: String?,
    val photoTitle: String?,
    val serverId: String?,
    val farmId: Int?,
    val secret: String?) {

    data class Builder(
        var photoId: String? = null,
        var ownerId: String? = null,
        var photoTitle: String? = null,
        var serverId: String? = null,
        var farmId: Int? = 0,
        var secret: String? = null) {

        fun photoId(photoId: String) = apply { this.photoId = photoId }
        fun ownerId(ownerId: String) = apply { this.ownerId = ownerId }
        fun photoTitle(photoTitle: String) = apply { this.photoTitle = photoTitle }
        fun serverId(serverId: String) = apply { this.serverId = serverId }
        fun farmId(farmId: Int) = apply { this.farmId = farmId }
        fun secret(secret: String) = apply { this.secret = secret }

        fun build() = SearchPhoto(photoId, ownerId, photoTitle, serverId, farmId, secret)
    }

}