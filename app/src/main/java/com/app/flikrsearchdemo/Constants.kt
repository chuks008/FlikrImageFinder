package com.app.flikrsearchdemo

/**
 * Created by Your name on 2019-11-05.
 */
class Constants {

    companion object {
        const val BASE_URL = "https://api.flickr.com/"
        const val IMAGE_SEARCH_METHOD = "flickr.photos.search"
        const val IMAGE_LOAD_URL = "${BASE_URL}photos/%s/%s"
        const val BACKGROUND_THREAD_KEY = "background"
        const val MAIN_THREAD_KEY = "main_thread"
    }
}