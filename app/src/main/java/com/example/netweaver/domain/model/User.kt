package com.example.netweaver.domain.model

data class User(
    val userId: String? = "",
    val email: String? = "",
    val fullName: String? = "",
    val headline: String? = "",
    val location: String? = "",
    val about: String? = "",
    val profileImageUrl: String? = ""
)