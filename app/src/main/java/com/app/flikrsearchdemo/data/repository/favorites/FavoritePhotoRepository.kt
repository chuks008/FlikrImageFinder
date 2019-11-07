package com.app.flikrsearchdemo.data.repository.favorites

import com.app.flikrsearchdemo.data.db.favorites.FavoritePhoto
import com.app.flikrsearchdemo.data.file_management.OnImageDownloadComplete
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Your name on 2019-11-06.
 */
interface FavoritePhotoRepository {

    fun getPhotos(): Single<List<FavoritePhoto>>
    fun addPhoto(photoTitle: String, photoUrl: String, onComplete: OnImageDownloadComplete)
    fun removePhoto(toRemove: FavoritePhoto): Completable
}