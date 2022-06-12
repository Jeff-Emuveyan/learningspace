package com.example.uitests_hilt.hilt

import android.content.Context
import com.example.uitests_hilt.data.source.remote.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideRetrofit(@ApplicationContext appContext: Context): Retrofit {
        return ApiClient.getClient(appContext)
    }
}