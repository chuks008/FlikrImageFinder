package com.app.flikrsearchdemo.presentation.adapter.photos

/**
 * Created by Your name on 2019-11-06.
 */
interface PhotoRow {

    fun setTitle(title: String)
    fun setImage(imageUrl: String)
    fun setPosition(position: Int)
}