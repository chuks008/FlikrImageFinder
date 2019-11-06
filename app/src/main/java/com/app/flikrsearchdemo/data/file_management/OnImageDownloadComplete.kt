package com.app.flikrsearchdemo.data.file_management

import com.app.flikrsearchdemo.data.db.favorites.FavoritePhoto
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Your name on 2019-11-06.
 */
interface OnImageDownloadComplete {

    fun onCompleteImageSave(error: Boolean, message: String, imageInsertOp: Completable?)
}