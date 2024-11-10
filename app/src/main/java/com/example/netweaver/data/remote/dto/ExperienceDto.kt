package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Experience
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExperienceDto(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("company_name")
    val companyName: String,
    @SerialName("position")
    val position: String,
    @SerialName("start_date")
    val startDate: Instant,
    @SerialName("end_date")
    val endDate: Instant,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant
)

fun Instant.toDateTime(): LocalDate = this.toLocalDateTime(TimeZone.currentSystemDefault()).date

fun ExperienceDto.toDomain() = Experience(
    id = id,
    userId = userId,
    companyName = companyName,
    position = position,
    startDate = startDate.toDateTime(),
    endDate = endDate.toDateTime()
)