package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Experience
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExperienceDto(
    @SerialName("id")
    val id: String = "",
    @SerialName("user_id")
    val userId: String = "",
    @SerialName("company_name")
    val companyName: String = "",
    @SerialName("position")
    val position: String = "",
    @SerialName("start_date")
    val startDate: Instant = now(),
    @SerialName("end_date")
    val endDate: Instant = now(),
    @SerialName("created_at")
    val createdAt: Instant = now(),
    @SerialName("updated_at")
    val updatedAt: Instant = now()
)

fun ExperienceDto.toDomain() = Experience(
    id = id,
    userId = userId,
    companyName = companyName,
    position = position,
    startDate = startDate,
    endDate = endDate
)