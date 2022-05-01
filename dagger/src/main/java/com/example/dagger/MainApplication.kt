package com.example.dagger

import android.app.Application
import android.content.Context
import com.example.dagger.di.ApplicationComponent
import com.example.dagger.di.DaggerApplicationComponent

open class MainApplication : Application() {

    lateinit var appComponent: ApplicationComponent
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        appComponent = DaggerApplicationComponent.factory().create(this)
    }
}