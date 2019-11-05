package com.app.flikrsearchdemo.application

import com.app.flikrsearchdemo.di.components.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Created by Your name on 2019-11-05.
 */
class FlikrDemoSearchApp: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}