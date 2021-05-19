package com.seamfix.carbonlist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MainApplication : Application(){

    open val baseUrl = "https://api.football-data.org/v2/"
}