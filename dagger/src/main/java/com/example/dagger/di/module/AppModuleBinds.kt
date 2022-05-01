package com.example.dagger.di.module

import androidx.lifecycle.ViewModelProvider
import com.example.dagger.di.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class AppModuleBinds {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}