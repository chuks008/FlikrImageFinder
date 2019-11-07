package com.app.flikrsearchdemo.data.file_management

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import io.reactivex.Single

/**
 * Created by Your name on 2019-11-06.
 */
interface ImageFileMgr {

    fun downloadImage(photoUrl: String): Single<Bitmap>
    fun getRealPathFromUri(context: Context, contentUri: Uri): String

}