package com.app.flikrsearchdemo.data.db.favorites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Your name on 2019-11-05.
 */
@Dao
interface FavoritePhotoDao {

    @Query("SELECT * FROM favorite_photos")
    fun getPhotos(): Single<List<FavoritePhoto>>

    @Insert
    fun addFavoritePhoto(favoritePhoto: FavoritePhoto): Completable

    @Delete
    fun deleteFavoritePhoto(favoritePhoto: FavoritePhoto)

}