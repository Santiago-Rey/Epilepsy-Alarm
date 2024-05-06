package com.sr.configuration.data.config

import android.app.Application
import androidx.room.Room

class UserApp: Application() {
    companion object{
        lateinit var db: UserDataBase
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            UserDataBase::class.java,
            "userDataBase"
        ).fallbackToDestructiveMigration().build()

    }
}