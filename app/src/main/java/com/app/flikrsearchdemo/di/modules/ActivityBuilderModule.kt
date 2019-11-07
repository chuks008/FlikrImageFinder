package com.app.flikrsearchdemo.di.modules

import com.app.flikrsearchdemo.presentation.favorites.FavoritePhotosActivity
import com.app.flikrsearchdemo.presentation.photo_search.PhotoSearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Your name on 2019-11-05.
 */
@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributesPhotoSearchActivity(): PhotoSearchActivity

    @ContributesAndroidInjector
    abstract fun contributesFavoritePhotoActivity(): FavoritePhotosActivity

}