package com.example.dagger.signup.di
import dagger.Module
import androidx.lifecycle.ViewModel
import com.example.dagger.di.ViewModelKey
import com.example.dagger.signup.ui.SignUpViewModel
import dagger.Binds
import dagger.multibindings.IntoMap

@Module
abstract class SignUpModule {

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindViewModel(viewmodel: SignUpViewModel): ViewModel
}