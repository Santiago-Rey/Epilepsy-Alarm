package com.sr.configuration.data.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sr.configuration.data.dao.UserDao
import com.sr.configuration.data.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class UserDataBase:  RoomDatabase() {
    abstract fun userDao(): UserDao

}