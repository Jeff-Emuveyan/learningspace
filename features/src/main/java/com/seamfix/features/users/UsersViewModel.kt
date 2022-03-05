package com.seamfix.features.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seamfix.core.model.table.UserEntity
import com.seamfix.core.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class UsersViewModel @Inject constructor(var userRepository: UserRepository) : ViewModel() {

    private val _users = MutableStateFlow(emptyList<UserEntity>())
    val users = _users.asStateFlow()

    fun save(userEntity: UserEntity) = viewModelScope.launch {
        userRepository.saveUser(userEntity)
    }

    fun getUserFormLocal() = userRepository.getUsersFromLocal()

    fun getFromRemote() = userRepository.getUserFromRemote()

}
