package com.app.flikrsearchdemo.data.file_management

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Single
import java.net.URL
import javax.inject.Inject

/**
 * Created by Your name on 2019-11-06.
 */
class FileSaverMgrImpl @Inject constructor(): FileSaverMgr {

    private fun generateBitmapFromUrl(downloadUrl: String): Bitmap {
        val url = URL(downloadUrl)
        return BitmapFactory.decodeStream(url.openConnection().getInputStream())
    }

    override fun downloadImage(photoUrl: String): Single<Bitmap> {
        return Single.fromCallable {
            generateBitmapFromUrl(photoUrl)
        }
    }
}