package com.app.flikrsearchdemo.data.db.favorites

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Your name on 2019-11-05.
 */

@Entity(tableName = "favorite_photos")
data class FavoritePhoto(val photoTitle: String, val photoLocation: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}