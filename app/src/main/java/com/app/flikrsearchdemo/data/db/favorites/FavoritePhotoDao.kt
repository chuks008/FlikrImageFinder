package com.app.flikrsearchdemo.data.db.favorites

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Created by Your name on 2019-11-05.
 */
@Dao
interface FavoritePhotoDao {

    @Query("SELECT * FROM favorite_photos")
    fun getPhotos(): List<FavoritePhoto>

    @Insert
    fun addFavoritePhoto(imageTitle: String, fileLocation: String)

    @Delete
    fun deleteFavoritePhoto(id: Int)

}