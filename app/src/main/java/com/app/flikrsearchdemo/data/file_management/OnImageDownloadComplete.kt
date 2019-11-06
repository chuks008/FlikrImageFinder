package com.app.flikrsearchdemo.data.file_management

/**
 * Created by Your name on 2019-11-06.
 */
interface OnImageDownloadComplete {

    fun onCompleteImageSave(error: Boolean, message: String)
}