package com.app.flikrsearchdemo.data.db

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.flikrsearchdemo.data.db.favorites.FavoritePhoto
import com.app.flikrsearchdemo.data.db.favorites.FavoritePhotoDao
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Your name on 2019-11-05.
 */

@Database(entities = [FavoritePhoto::class], version = 1)
abstract class FlikrDemoDB: RoomDatabase() {

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
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}