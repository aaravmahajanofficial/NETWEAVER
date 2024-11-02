package com.example.netweaver.data.remote.dto

import com.example.netweaver.domain.model.User
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id")
    val userId: String,
    @SerialName("email")
    val email: String?,
    @SerialName("first_name")
    val firstName: String = "",
    @SerialName("last_name")
    val lastName: String = "",
    @SerialName("headline")
    val headline: String? = "",
    @SerialName("location")
    val location: String? = "",
    @SerialName("about")
    val about: String? = "",
    @SerialName("profile_image_url")
    val profileImageUrl: String? = "",
    @SerialName("created_at")
    val createdAt: Instant?,
    @SerialName("updated_at")
    val updatedAt: Instant? = now()
) {
    val fullName: String
        get() = "$firstName $lastName"
}

fun UserDto.toDomain() = User(
    userId = userId,
    email = email,
    fullName = fullName,
    headline = headline,
    location = headline,
    about = about,
    profileImageUrl = profileImageUrl
)