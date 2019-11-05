package com.app.flikrsearchdemo.di.components

import android.app.Application
import com.app.flikrsearchdemo.application.FlikrDemoSearchApp
import com.app.flikrsearchdemo.di.modules.ActivityBuilderModule
import com.app.flikrsearchdemo.di.modules.AppModule
import com.app.flikrsearchdemo.di.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Created by Your name on 2019-11-05.
 */
@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    ActivityBuilderModule::class,
    NetworkModule::class])

interface AppComponent: AndroidInjector<FlikrDemoSearchApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}