package com.example.uitests_hilt.hilt
import com.example.uitests_hilt.data.source.remote.Service
import com.example.uitests_hilt.model.dto.CitiesResponse
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Query
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun bindService(serviceImpl: ServiceImpl): Service
}

class ServiceImpl @Inject constructor(var retrofit: Retrofit): Service {
    override suspend fun getCitiesByPageNumber(pageNumber: Int, country: String): Response<CitiesResponse> {
        return retrofit.create(Service::class.java).getCitiesByPageNumber(pageNumber, country)
    }

    override suspend fun getCitiesByCityName(cityName: String,
                                             country: String): Response<CitiesResponse> {
        return retrofit.create(Service::class.java).getCitiesByCityName(cityName, country)
    }
}