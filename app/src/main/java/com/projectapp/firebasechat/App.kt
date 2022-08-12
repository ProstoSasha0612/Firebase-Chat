package com.projectapp.firebasechat

import android.app.Application
import android.content.Context
import com.projectapp.firebasechat.di.AppComponent
import com.projectapp.firebasechat.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> this.appComponent
        else -> this.applicationContext.appComponent
    }
