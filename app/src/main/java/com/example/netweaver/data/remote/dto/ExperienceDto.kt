package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.Experience
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ExperienceDto(
    val id: String,
    val userId: String,
    val companyName: String,
    val position: String,
    val startDate: Instant,
    val endDate: Instant,
    val description: String,
    val createdAt: Instant,
    val updatedAt: Instant
)

fun Instant.toDateTime(): LocalDate = this.toLocalDateTime(TimeZone.currentSystemDefault()).date

fun ExperienceDto.toDomain() = Experience(
    id = id,
    userId = userId,
    companyName = companyName,
    position = position,
    description = description,
    startDate = startDate.toDateTime(),
    endDate = endDate.toDateTime()
)