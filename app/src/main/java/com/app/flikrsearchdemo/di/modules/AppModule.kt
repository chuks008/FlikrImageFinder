package com.app.flikrsearchdemo.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.app.flikrsearchdemo.Constants
import com.app.flikrsearchdemo.data.repository.photos_search.PhotoSearchRepository
import com.app.flikrsearchdemo.data.repository.photos_search.PhotoSearchRepositoryImpl
import com.app.flikrsearchdemo.data.repository.search_terms.SearchTermRepository
import com.app.flikrsearchdemo.data.repository.search_terms.SearchTermRepositoryImpl
import com.app.flikrsearchdemo.executors.AppTaskExecutor
import com.app.flikrsearchdemo.executors.BackgroundExecutor
import com.app.flikrsearchdemo.executors.PostTaskExecutor
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Your name on 2019-11-05.
 */

@Module
class AppModule {

    @Provides
    @Singleton
    @Named(Constants.MAIN_THREAD_KEY)
    fun providesUIThread(postTaskExecutor: PostTaskExecutor): AppTaskExecutor {
        return postTaskExecutor
    }

    @Provides
    @Singleton
    @Named(Constants.BACKGROUND_THREAD_KEY)
    fun providesBackgroundThread(backgroundExecutor: BackgroundExecutor): AppTaskExecutor {
        return backgroundExecutor
    }

    @Provides
    fun providesSharedPreferences(context: Application): SharedPreferences {
        return context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    fun providesPhotoSearchRepo(photoSearchRepo: PhotoSearchRepositoryImpl): PhotoSearchRepository {
        return photoSearchRepo
    }

    @Provides
    fun providesSearchTermRepository(searchTermRepository: SearchTermRepositoryImpl): SearchTermRepository {
        return searchTermRepository
    }

}