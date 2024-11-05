package com.example.netweaver.domain.model

data class User(
    val userId: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val fullName: String = "",
    val headline: String? = null,
    val location: String? = null,
    val about: String? = null,
    val profileImageUrl: String? = null
)