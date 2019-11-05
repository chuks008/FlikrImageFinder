package com.app.flikrsearchdemo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.flikrsearchdemo.data.db.favorites.FavoritePhoto
import com.app.flikrsearchdemo.data.db.favorites.FavoritePhotoDao
import com.app.flikrsearchdemo.data.db.search_tags.SearchTag
import com.app.flikrsearchdemo.data.db.search_tags.SearchTagDao
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Your name on 2019-11-05.
 */

@Database(entities = [SearchTag::class, FavoritePhoto::class], version = 1)
public abstract class FlikrDemoDB: RoomDatabase() {

    abstract fun searchTagDao(): SearchTagDao
    abstract fun favoritePhotoDao(): FavoritePhotoDao

    companion object {
        @Volatile
        private var INSTANCE: FlikrDemoDB? = null

        fun getDatabase(context: Context): FlikrDemoDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlikrDemoDB::class.java,
                    "flikr_demo_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}