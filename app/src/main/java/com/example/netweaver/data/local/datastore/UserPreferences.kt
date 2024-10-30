package com.example.netweaver.data.local.datastore

data class UserPreferences(
    val userId: String = "",
    val token: String = "",
    val email: String = ""
)