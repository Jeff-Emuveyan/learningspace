package com.example.uitests_hilt.data.source.local

import com.example.uitests_hilt.data.source.local.dao.CityDao
import com.example.uitests_hilt.model.entity.CityEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityLocalDataSource @Inject constructor(private val cityDao: CityDao,
                                              private val ioDispatcher: CoroutineDispatcher): ICityLocalDataSource {

    override suspend fun save(list: List<CityEntity>) = withContext(ioDispatcher) {
        for (cityEntity in list) {
            cityDao.save(cityEntity)
        }
    }

    override fun getCitiesByPageNumber(pageNumber: Int) =
        cityDao.getAllByPageNumber(pageNumber)


    override fun getCitiesByCityName(cityName: String) =
        cityDao.getAllByName("%${cityName}%")
}