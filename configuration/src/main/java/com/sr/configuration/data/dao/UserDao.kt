package com.sr.configuration.data.dao

import androidx.room.*
import com.sr.configuration.data.User

@Dao
interface UserDao {

    @Query("SELECT * FROM User WHERE id = :idUser limit 1")
    suspend fun getUser(idUser: Int) : User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

}