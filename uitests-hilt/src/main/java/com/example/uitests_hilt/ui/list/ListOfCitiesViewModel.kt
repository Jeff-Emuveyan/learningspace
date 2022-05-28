package com.example.uitests_hilt.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uitests_hilt.data.repository.CityRepository
import com.example.uitests_hilt.model.dto.Query
import com.example.uitests_hilt.model.dto.Result
import com.example.uitests_hilt.model.dto.ui.UIStateType.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListOfCitiesViewModel @Inject constructor(private val repository: CityRepository) : ViewModel() {

    private var currentPageNumber = 1

    private val _uiState = MutableStateFlow(Result(DEFAULT))
    val uiState = _uiState.asStateFlow()

    private val _longRunningTaskResult = MutableSharedFlow<String>()
    val longRunningTaskResult = _longRunningTaskResult.asSharedFlow()

    fun getCities(query: Query) = viewModelScope.launch {
        if (_uiState.value.type == LOADING) return@launch

        repository.fetchAndCacheCities(query).onStart {
            _uiState.value = Result(LOADING)
        }.collect {
            handleResult(query, it)
        }
    }

    private fun handleResult(query: Query, result: Result) {
        if (result.type == SUCCESS && (query.value is Int)) {
            currentPageNumber = query.value as Int
        }
        _uiState.value = result
    }

    fun getNextPageNumber() = currentPageNumber + 1

    fun doLongRunningTask() = viewModelScope.launch {
        val result = repository.mimicALongRunningTask()
        _longRunningTaskResult.emit(result)
    }
}