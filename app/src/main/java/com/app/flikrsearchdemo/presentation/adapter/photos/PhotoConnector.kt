package com.app.flikrsearchdemo.presentation.adapter.photos

/**
 * Created by Your name on 2019-11-06.
 */
interface PhotoConnector {
    fun bind(photo: PhotoRow, position: Int)
    fun getItemCount(): Int
    fun onSelectItem(position: Int)
}