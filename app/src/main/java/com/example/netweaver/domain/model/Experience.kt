package com.example.netweaver.domain.model

import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.Instant

data class Experience(
    val id: String = "",
    val userId: String = "",
    val companyName: String = "",
    val position: String = "",
    val startDate: Instant = now(),
    val endDate: Instant? = now()
)
