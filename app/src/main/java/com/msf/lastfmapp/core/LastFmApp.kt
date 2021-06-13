package com.msf.lastfmapp.core

import android.app.Application
import com.msf.lastfmapp.di.LastFmDi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LastFmApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LastFmApp)
            modules(LastFmDi.module)
        }
    }
}