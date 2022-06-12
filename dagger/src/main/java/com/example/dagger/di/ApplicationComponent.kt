package com.example.dagger.di

import android.content.Context
import com.example.dagger.di.module.AppModule
import com.example.dagger.di.module.AppModuleBinds
import com.example.dagger.signup.di.SignUpModule
import com.example.dagger.signup.ui.SignUpActivity
import com.example.dagger.signup.ui.SignUpFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class, AppModuleBinds::class, SignUpModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }

    fun inject(signUpActivity: SignUpActivity)
    fun inject(signUpFragment: SignUpFragment)
}