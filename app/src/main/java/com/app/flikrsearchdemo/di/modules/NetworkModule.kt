package com.app.flikrsearchdemo.di.modules

import com.app.flikrsearchdemo.Constants
import com.app.flikrsearchdemo.data.repository.FlikrApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Your name on 2019-11-05.
 */

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun providesGsonConverter(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun providesRxAdapter(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun providesRetrofit(converterFactory: GsonConverterFactory,
                         rxJava2CallAdapterFactory: RxJava2CallAdapterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun providesFlikrAPI(retrofit: Retrofit): FlikrApi {
        return retrofit.create(FlikrApi::class.java)
    }

}