package com.example.uitests_hilt.hilt

import com.example.uitests_hilt.util.idlingresource.EspressoIdlingResource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EspressoIdlingResourceModule {

    @Singleton
    @Provides
    fun provideEspressoIdlingResource(): EspressoIdlingResource {
        return EspressoIdlingResource
    }
}