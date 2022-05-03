package com.example.uitests_hilt.data.source.local

import com.example.uitests_hilt.model.dto.CitiesResponse
import com.example.uitests_hilt.model.entity.CityEntity
import kotlinx.coroutines.flow.Flow

interface ICityLocalDataSource {
    
    suspend fun save(list: List<CityEntity>)

    fun getCitiesByPageNumber(pageNumber: Int = 1): List<CityEntity>

    fun getCitiesByCityName(cityName: String): List<CityEntity>
}