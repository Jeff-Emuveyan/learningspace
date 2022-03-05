package com.seamfix.core.repositories

import com.seamfix.core.model.table.UserEntity
import com.seamfix.core.source.local.dao.UserDao
import com.seamfix.core.source.remote.Service
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class UserRepository @Inject constructor(private val ioDispatcher: CoroutineDispatcher,
                                              private val service: Service,
                                              private val userDao: UserDao) {

    suspend fun saveUser(userEntity: UserEntity) = withContext(ioDispatcher) {
        userDao.saveUser(userEntity)
    }

    fun getUsersFromLocal() = userDao.getAllUsers()

    fun getUserFromRemote() = flow {
        while (true) {
            delay(3_000)
            val random = Math.random()
            emit(UserEntity(firstName = "Jeff--${random}"))
        }
    }.flowOn(Dispatchers.IO) // We use this since our fragment is using Dispatchers.MAIN
}