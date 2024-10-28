package com.example.netweaver.domain.model

data class User(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val headline: String,
    val location: String,
    val about: String,
    val profileImageUrl: String
)