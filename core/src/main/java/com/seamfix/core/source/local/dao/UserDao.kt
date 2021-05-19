package com.seamfix.core.source.local.dao

import androidx.annotation.Keep
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.seamfix.core.model.table.User

@Keep
@Dao
interface UserDao {

    @Insert
    suspend fun saveUser(user: User)

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserByID(id: String): User?

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>?

    @Update
    suspend fun updateUser(user: User)
}