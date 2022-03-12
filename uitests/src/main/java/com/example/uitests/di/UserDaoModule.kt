package com.example.uitests.di
import com.seamfix.core.model.response.UserDto
import com.seamfix.core.model.response.UserResponse
import com.seamfix.core.model.table.UserEntity
import com.seamfix.core.source.local.AppDatabase
import com.seamfix.core.source.local.dao.UserDao
import com.seamfix.core.source.remote.Service
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject


class UserDaoImpl @Inject constructor(var appDatabase: AppDatabase): UserDao
{
    override suspend fun saveUser(user: UserEntity) {
        appDatabase.userDao().saveUser(user)
    }

    override fun getAllUsers(): Flow<List<UserEntity>> {
        return appDatabase.userDao().getAllUsers()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class UserDaoModule {
    @Binds
    abstract fun bind(userDaoImpl: UserDaoImpl): UserDao
}