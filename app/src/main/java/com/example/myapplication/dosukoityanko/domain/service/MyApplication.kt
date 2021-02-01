package com.example.myapplication.dosukoityanko.domain.service

import android.app.Application
import androidx.room.Room
import timber.log.Timber

class MyApplication : Application() {

    companion object {
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "AppDatabase"
        ).build()
    }
}
