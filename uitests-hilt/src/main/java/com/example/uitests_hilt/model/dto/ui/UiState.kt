package com.example.uitests_hilt.model.dto.ui

import com.example.uitests_hilt.model.dto.QueryType
import com.example.uitests_hilt.model.entity.CityEntity
import java.lang.Exception

enum class UIStateType{
    DEFAULT, NETWORK_ERROR, SUCCESS, NO_RESULT, LOADING, LOADING_LONG_TASK, COMPLETED_LONG_TASK
}