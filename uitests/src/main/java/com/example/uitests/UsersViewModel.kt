package com.example.uitests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seamfix.core.model.response.EmployeeResponse
import com.seamfix.core.model.response.GiftResponse
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

    private val _gift = MutableSharedFlow<GiftResponse?>()
    val gift = _gift.asSharedFlow()

    fun save(employeeEntity: UserEntity) = viewModelScope.launch {
        employeeRepository.saveUser(employeeEntity)
    }

    fun getUserFormLocal() = employeeRepository.getUsersFromLocal()

    fun getFromRemote() = employeeRepository.getUserFromRemote()

    fun fetchEmployees()  = employeeRepository.getEmployeeFromRemote().onEach {
        _employee.value = it
    }.launchIn(viewModelScope)

    fun fetchGifts()  = employeeRepository.getGiftFromRemote().onEach {
        _gift.emit(it)
    }.launchIn(viewModelScope)

}
