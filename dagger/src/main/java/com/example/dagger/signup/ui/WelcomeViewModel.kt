package com.example.dagger.signup.ui

import androidx.lifecycle.ViewModel
import com.example.dagger.util.AppIDGenerator
import com.example.dagger.util.SessionIDGenerator
import com.example.dagger_core.repositories.UserRepository
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(private val appIDGenerator: AppIDGenerator,
                                           private val sessionIdGenerator: SessionIDGenerator,
                                           private val userRepository: UserRepository) : ViewModel() {

    fun getSessionId() = sessionIdGenerator.getId

    fun getApplicationId() = appIDGenerator.getId
}