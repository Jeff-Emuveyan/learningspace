package com.seamfix.core.source.local.dao

import androidx.annotation.Keep
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.seamfix.core.model.table.UserEntity
import kotlinx.coroutines.flow.Flow

@Keep
@Dao
interface UserDao {

    @Insert
    suspend fun saveUser(user: UserEntity)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<UserEntity>>

}