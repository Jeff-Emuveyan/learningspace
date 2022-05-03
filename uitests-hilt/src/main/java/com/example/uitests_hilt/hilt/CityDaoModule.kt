package com.example.uitests_hilt.hilt
import com.example.uitests_hilt.data.source.local.AppDatabase
import com.example.uitests_hilt.data.source.local.dao.CityDao
import com.example.uitests_hilt.model.entity.CityEntity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
abstract class CityDaoModule {
    @Binds
    abstract fun bindService(cityImpl: CityDaoImpl): CityDao
}

class CityDaoImpl @Inject constructor(val db: AppDatabase): CityDao {
    override suspend fun save(cityEntity: CityEntity) {
        return db.cityDao().save(cityEntity)
    }

    override fun getMostRecent(numberOfItems: Int): List<CityEntity> {
        return db.cityDao().getMostRecent(numberOfItems)
    }

    override fun getAllByPageNumber(pageNumber: Int): List<CityEntity> {
        return db.cityDao().getAllByPageNumber(pageNumber)
    }

    override fun getAllByName(cityName: String): List<CityEntity> {
        return db.cityDao().getAllByName(cityName)
    }
}