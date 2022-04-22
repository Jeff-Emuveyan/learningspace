package com.example.dagger.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dagger.util.IDGenerator
import com.seamfix.core.model.table.UserEntity
import com.seamfix.core.repositories.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

/*class SignUpViewModel @Inject constructor(val idGenerator: IDGenerator,
                                          val userRepository: UserRepository) : ViewModel() {

    fun sighUpUser(name: String, age: Int) = viewModelScope.launch {
        val user = UserEntity(idGenerator.getId!!, name, age)
        userRepository.saveUser(user)
    }
}*/

class SignUpViewModel @Inject constructor() : ViewModel() {

    fun sighUpUser(name: String, age: Int) = viewModelScope.launch {

    }
}