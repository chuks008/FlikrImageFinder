package com.app.flikrsearchdemo.data.file_management

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import io.reactivex.Single
import java.net.URL
import javax.inject.Inject
import android.provider.MediaStore
import androidx.room.util.CursorUtil.getColumnIndexOrThrow



/**
 * Created by Your name on 2019-11-06.
 */
class ImageFileMgrImpl @Inject constructor(): ImageFileMgr {

    private fun generateBitmapFromUrl(downloadUrl: String): Bitmap {
        val url = URL(downloadUrl)
        return BitmapFactory.decodeStream(url.openConnection().getInputStream())
    }

    override fun downloadImage(photoUrl: String): Single<Bitmap> {
        return Single.fromCallable {
            generateBitmapFromUrl(photoUrl)
        }
    }

    override fun getRealPathFromUri(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

}