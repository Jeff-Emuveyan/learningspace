package com.example.uitests_hilt.model.dto

import com.example.uitests_hilt.model.entity.CityEntity

data class City(
    val lng: Double? = null,
    val updatedAt: String? = null,
    val name: String? = null,
    val createdAt: String? = null,
    val id: Int? = null,
    val local_name: String? = null,
    val lat: Double? = null,
    val countryId: Int? = null,
    val country: Country? = null
) {

    fun toCityEntity(pageNumber: Int): CityEntity? {
        return if (id == null) null else CityEntity(lng, name, id, local_name, lat, pageNumber, countryName = country?.name)
    }
}