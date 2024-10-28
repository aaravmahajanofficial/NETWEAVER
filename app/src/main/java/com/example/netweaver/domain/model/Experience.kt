package com.example.netweaver.domain.model

import kotlinx.datetime.LocalDate

data class Experience(
    val id: String,
    val userId: String,
    val companyName: String,
    val position: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate? = null
)
