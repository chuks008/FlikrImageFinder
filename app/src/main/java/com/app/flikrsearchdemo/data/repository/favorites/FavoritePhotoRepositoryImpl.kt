package com.app.flikrsearchdemo.data.repository.favorites

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import com.app.flikrsearchdemo.data.db.FlikrDemoDB
import com.app.flikrsearchdemo.data.db.favorites.FavoritePhoto
import com.app.flikrsearchdemo.data.file_management.ImageFileMgr
import com.app.flikrsearchdemo.data.file_management.OnImageDownloadComplete
import com.app.flikrsearchdemo.executors.BackgroundExecutor
import com.app.flikrsearchdemo.executors.PostTaskExecutor
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import java.net.URI
import javax.inject.Inject

/**
 * Created by Your name on 2019-11-06.
 */
class FavoritePhotoRepositoryImpl @Inject constructor (private val db: FlikrDemoDB,
                                                       private val context: Application,
                                                       private val backgroundExecutor: BackgroundExecutor,
                                                       private val postTaskExecutor: PostTaskExecutor,
                                                       private val imageFileMgr: ImageFileMgr):
    FavoritePhotoRepository {

    private val TAG = FavoritePhotoRepository::class.java.simpleName

    override fun getPhotos(): Single<List<FavoritePhoto>> {
        return db.favoritePhotoDao().getPhotos()
    }

    override fun addPhoto(photoTitle: String,
                          photoUrl: String,
                          onComplete: OnImageDownloadComplete) {
        // save to local storage using file saver manager

        imageFileMgr.downloadImage(photoUrl)
            .subscribeOn(backgroundExecutor.scheduler)
            .observeOn(postTaskExecutor.scheduler)
            .subscribe(object : SingleObserver<Bitmap> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(bitmap: Bitmap) {

                    try {
                        val location = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, photoTitle , photoTitle)

                        val photoSaveTask = db.favoritePhotoDao()
                            .addFavoritePhoto(FavoritePhoto(photoTitle, location))

                        Log.e(TAG, "Success saving picture at $location")
                        onComplete.onCompleteImageSave(false,
                            "Success saving image to local storage", photoSaveTask)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        onComplete.onCompleteImageSave(true, e.localizedMessage!!, null)
                    }
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "Error saving image: ${e.localizedMessage}")
                    onComplete.onCompleteImageSave(true, e.localizedMessage!!, null)
                }
            })
    }

    override fun removePhoto(toRemove: FavoritePhoto): Completable {

        try {
            Log.e(TAG, "Image location: ${toRemove.photoLocation}")
            Log.e(TAG, "Actual image location: ${Uri.parse(toRemove.photoLocation).path}")
//           DocumentFile.fromSingleUri(context, Uri.parse(toRemove.photoLocation))?.delete()
            val convertedToFileString = imageFileMgr.getRealPathFromUri(context, Uri.parse(toRemove.photoLocation))
            val file = File(convertedToFileString)
            if(file.exists()) {
                file.delete()
                Log.e(TAG, "File deleted")
            } else {
                Log.e(TAG, "Could not delete file")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return Completable.error(e)
        }

        return db.favoritePhotoDao().deleteFavoritePhoto(toRemove)
    }


}