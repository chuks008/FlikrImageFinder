package com.app.flikrsearchdemo.data.db.search_tags

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Your name on 2019-11-05.
 */

@Entity(tableName = "search_tags")
data class SearchTag(val keyword: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}