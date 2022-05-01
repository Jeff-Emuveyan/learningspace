package com.example.dagger.di.module

import android.content.Context
import com.example.dagger_core.source.local.AppDatabase
import com.example.dagger_core.source.local.dao.UserDao
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideIOCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

   // So that only one instance of it exist
    @Provides
    fun provedAppDatabase(context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun providesUserDAO(appDatabase: AppDatabase): UserDao = appDatabase.userDao()
}