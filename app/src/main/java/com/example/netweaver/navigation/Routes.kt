package com.example.netweaver.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val route: String) {

    data object Home : Routes("home")
    data object MyNetwork : Routes("myNetwork")
    data object Post : Routes("post")
    data object Notifications : Routes("notifications")
    data object Jobs : Routes("jobs")
}