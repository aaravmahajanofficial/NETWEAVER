package com.example.netweaver.data.remote.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class EducationDto(
    val id: String,
    val userId: String,
    val school: String,
    val degree: String,
    val field: String,
    val startDate: Instant,
    val endDate: Instant
)
