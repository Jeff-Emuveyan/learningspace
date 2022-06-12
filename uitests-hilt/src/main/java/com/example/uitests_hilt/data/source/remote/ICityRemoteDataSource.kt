package com.example.uitests_hilt.data.source.remote

import com.example.uitests_hilt.model.dto.CitiesResponse
import kotlinx.coroutines.flow.Flow

interface ICityRemoteDataSource {

    suspend fun getCitiesByPageNumber(pageNumber: Int = 1): CitiesResponse?

    suspend fun getCitiesByCityName(cityName: String): CitiesResponse?
}