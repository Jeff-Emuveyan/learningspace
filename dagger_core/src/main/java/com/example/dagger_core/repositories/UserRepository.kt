package com.example.dagger_core.repositories

import com.example.dagger_core.model.response.EmployeeResponse
import com.example.dagger_core.model.response.GiftResponse
import com.example.dagger_core.model.table.UserEntity
import com.example.dagger_core.source.local.dao.UserDao
import com.example.dagger_core.source.remote.Service
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

open class UserRepository @Inject constructor(private val ioDispatcher: CoroutineDispatcher,
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
    }.flowOn(ioDispatcher) // We use this since our fragment is using Dispatchers.MAIN

    fun getEmployeeFromRemote() = flow {
        while (true) {
            delay(3_000)
            val random = Math.random()
            emit(EmployeeResponse(name = "Jane--${random}", salary = Random.nextLong()))
        }
    }.flowOn(ioDispatcher) // We use this since our fragment is using Dispatchers.MAIN

    fun getGiftFromRemote() = flow {
        while (true) {
            delay(3_000)
            val random = Math.random()
            emit(GiftResponse(name = "Samsung", cost = 123))
        }
    }.flowOn(ioDispatcher) // We use this since our fragment is using Dispatchers.MAIN
}