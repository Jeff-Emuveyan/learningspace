package com.seamfix.features.users.di

import android.content.Context
import com.seamfix.core.source.remote.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object RetrofitModule {

    @Provides
    fun provideRetrofit(@ApplicationContext appContext: Context): Retrofit {
        return ApiClient.getClient(appContext)
    }
}