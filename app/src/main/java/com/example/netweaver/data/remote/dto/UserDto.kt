package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.User
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val userId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val headline: String,
    val location: String,
    val about: String,
    val profileImageUrl: String,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    val fullName: String
        get() = "$firstName $lastName"
}

fun UserDto.toDomain() = User(
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    headline = headline,
    location = headline,
    about = about,
    profileImageUrl = profileImageUrl
)