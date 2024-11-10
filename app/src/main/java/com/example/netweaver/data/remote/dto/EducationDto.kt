package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Education
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EducationDto(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("school")
    val school: String,
    @SerialName("degree")
    val degree: String,
    @SerialName("field")
    val field: String,
    @SerialName("start_date")
    val startDate: Instant,
    @SerialName("end_date")
    val endDate: Instant
)

fun EducationDto.toDomain() = Education(
    id = id,
    userId = userId,
    school = school,
    degree = degree,
    field = field,
    startDate = startDate,
    endDate = endDate
)