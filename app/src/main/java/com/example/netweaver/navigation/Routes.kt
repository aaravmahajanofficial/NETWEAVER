package com.example.netweaver.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val route: String) {

    object Home : Routes("home")
    object MyNetwork : Routes("myNetwork")
    object Post : Routes("post")
    object Notifications : Routes("notifications")
    object Jobs : Routes("jobs")
    object Messages : Routes("messages")
    object Profile : Routes("profile")
    object CreatePost : Routes("createPost")
    object Login : Routes("loginPage")
    object Register : Routes("registerPage")
    object ForgotPassword : Routes("forgotPassword")
}