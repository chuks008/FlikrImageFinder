package com.app.flikrsearchdemo.data.db.search_tags

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Created by Your name on 2019-11-05.
 */
@Dao
interface SearchTagDao {

    @Query("SELECT * FROM search_tags")
    fun getSearchTags(): List<SearchTag>

    @Insert
    fun inserSearchTag()

    @Delete
    fun removeSearchTag()
}