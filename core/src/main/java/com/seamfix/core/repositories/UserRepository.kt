package com.seamfix.core.repositories

import com.seamfix.core.model.response.UserDto
import com.seamfix.core.model.response.UserResponse
import com.seamfix.core.model.table.User
import com.seamfix.core.source.local.AppDatabase
import com.seamfix.core.source.remote.Service
import javax.inject.Inject

open class UserRepository @Inject constructor(private val service: Service, private val database: AppDatabase) {

    open suspend fun save(newUser: User){
        val existingUser = database.userDao().getUserByID(newUser.id)
        if(existingUser == null){
            database.userDao().saveUser(newUser)
        }else{
            database.userDao().updateUser(newUser)
        }
    }

    open suspend fun getUserFromRemoteSource(userID: String): UserDto?{
        return try {
            val response = service.fetchUser(userID) ?: return null
            if(response.code() == 200 && response.body() != null){
                val  userResponse = response.body() as UserDto
                userResponse
            }else{
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    open suspend fun getUserFromLocalSource(userID: String): User?{
        return database.userDao().getUserByID(userID)
    }


    open suspend fun getUsersFromRemoteSource(): ArrayList<UserDto>?{

        return try {
            val response = service.fetchUsers() ?: return null
            if(response.code() == 200 && response.body() != null){
                val  userResponse = response.body() as UserResponse
                userResponse.data?.toCollection(ArrayList())
            }else{
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    open suspend fun getUsersFromLocalSource(): List<User>?{
        return database.userDao().getAllUsers()
    }
}