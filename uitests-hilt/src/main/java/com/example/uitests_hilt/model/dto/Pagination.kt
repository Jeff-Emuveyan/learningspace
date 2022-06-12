package com.example.uitests_hilt.model.dto

data class Pagination(
    val per_page: Int? = null,
    val total: Int? = null,
    val last_page: Int? = null,
    val current_page: Int? = null
)