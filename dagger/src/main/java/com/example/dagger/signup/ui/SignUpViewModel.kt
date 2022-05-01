package com.example.dagger.signup.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dagger.util.AppIDGenerator
import com.example.dagger.util.UserIDGenerator
import com.example.dagger_core.model.table.UserEntity
import com.example.dagger_core.repositories.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val appIDGenerator: AppIDGenerator,
                                          private val userIdGenerator: UserIDGenerator,
                                          private val userRepository: UserRepository) : ViewModel() {

    fun sighUpUser(name: String, age: Int) = viewModelScope.launch {
        val user = UserEntity(userIdGenerator.getId!!, name, age)
        userRepository.saveUser(user)
    }

    fun getUserId() = userIdGenerator.getId

    fun getApplicationId() = appIDGenerator.getId
}