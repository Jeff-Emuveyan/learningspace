package com.seamfix.features.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seamfix.core.model.response.EmployeeResponse
import com.seamfix.core.model.table.UserEntity
import com.seamfix.core.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class UsersViewModel @Inject constructor(private var employeeRepository: UserRepository) : ViewModel() {

    private val _employee = MutableStateFlow<EmployeeResponse?>(null)
    val employee = _employee.asStateFlow()

    fun save(employeeEntity: UserEntity) = viewModelScope.launch {
        employeeRepository.saveUser(employeeEntity)
    }

    fun getUserFormLocal() = employeeRepository.getUsersFromLocal()

    fun getFromRemote() = employeeRepository.getUserFromRemote()

    fun fetchEmployees()  = employeeRepository.getEmployeeFromRemote().onEach {
        _employee.value = it
    }.launchIn(viewModelScope)

}
