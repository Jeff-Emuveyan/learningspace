package com.example.uitests_hilt.model.dto

import com.example.uitests_hilt.model.dto.ui.UIStateType
import com.example.uitests_hilt.model.entity.CityEntity

class Result(val type: UIStateType, val cities: List<CityEntity>? = null)