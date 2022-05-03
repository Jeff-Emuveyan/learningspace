package com.example.uitests_hilt.data.source.remote

import com.example.uitests_hilt.model.dto.CitiesResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRemoteDataSource @Inject constructor (private val service: Service,
                                                private val ioDispatcher: CoroutineDispatcher): ICityRemoteDataSource {

    override suspend fun getCitiesByPageNumber(pageNumber: Int): CitiesResponse? = withContext(ioDispatcher) {
        try {
            val result = service.getCitiesByPageNumber(pageNumber)
            if (result.isSuccessful) result.body() else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getCitiesByCityName(cityName: String): CitiesResponse? = withContext(ioDispatcher) {
        try {
            val result = service.getCitiesByCityName(cityName)
            if (result.isSuccessful) result.body() else null
        } catch (e: Exception) {
            null
        }
    }
}