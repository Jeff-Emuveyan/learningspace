package com.seamfix.features.users.di
import com.seamfix.core.model.response.UserDto
import com.seamfix.core.model.response.UserResponse
import com.seamfix.core.source.remote.Service
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject


class ServiceImpl @Inject constructor(var retrofit: Retrofit): Service {

    override suspend fun fetchUsers(): Response<UserResponse> {
        return retrofit.create(Service::class.java).fetchUsers()
    }

    override suspend fun fetchUser(userId: String): Response<UserDto> {
        return retrofit.create(Service::class.java).fetchUser(userId)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class SServiceModule {
    @Binds
    abstract fun bindService(serviceImpl: ServiceImpl): Service
}