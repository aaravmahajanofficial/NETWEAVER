package com.example.netweaver.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.netweaver.ui.features.createPost.CreatePostScreen
import com.example.netweaver.ui.features.home.HomeScreen
import com.example.netweaver.ui.features.jobs.JobsScreen
import com.example.netweaver.ui.features.messages.MessagesScreen
import com.example.netweaver.ui.features.mynetwork.MyNetworkScreen
import com.example.netweaver.ui.features.notifications.NotificationsScreen
import com.example.netweaver.ui.features.profile.ProfileScreen

sealed class NavigationEvent {
    object NavigationToHome : NavigationEvent()
    object NavigationToMyNetwork : NavigationEvent()
    object NavigationToPost : NavigationEvent()
    object NavigationToNotifications : NavigationEvent()
    object NavigationToJobs : NavigationEvent()
    object NavigationToMessages : NavigationEvent()
    object NavigationToProfile : NavigationEvent()
}


class AppNavigator(private val navController: NavHostController) {

    val controller: NavController
        get() = navController

    fun navigateTo(event: NavigationEvent) {
        when (event) {
            NavigationEvent.NavigationToHome -> navController.navigate(route = Routes.Home.route)
            NavigationEvent.NavigationToMyNetwork -> navController.navigate(route = Routes.Home.route)
            NavigationEvent.NavigationToJobs -> navController.navigate(route = Routes.Jobs.route)
            NavigationEvent.NavigationToNotifications -> navController.navigate(route = Routes.Notifications.route)
            NavigationEvent.NavigationToPost -> navController.navigate(route = Routes.Post.route)
            NavigationEvent.NavigationToMessages -> navController.navigate(route = Routes.Messages.route)
            NavigationEvent.NavigationToProfile -> navController.navigate(route = Routes.Profile.route)
        }
    }
}

val LocalAppNavigator = compositionLocalOf<AppNavigator> {
    error("No NavController provided")
}

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    val appNavigator = remember { AppNavigator(navController) }

    CompositionLocalProvider(LocalAppNavigator provides appNavigator) {
        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 150)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 150)
                )
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = 150)
                )
            },
            popExitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = 150)
                )
            }
        ) {

            composable(route = Routes.Home.route) {

                HomeScreen()

            }
            composable(route = Routes.MyNetwork.route) {

                MyNetworkScreen()

            }
            composable(route = Routes.Post.route) {

                HomeScreen()

            }
            composable(route = Routes.Notifications.route) {

                NotificationsScreen()

            }
            composable(route = Routes.Jobs.route) {

                JobsScreen()

            }

            composable(route = Routes.Messages.route) {

                MessagesScreen()

            }

            composable(route = Routes.Profile.route) {
                ProfileScreen()
            }

            composable(route = Routes.CreatePost.route) {
                CreatePostScreen(
                    onNavigateBack = {
                        navController.navigate(route = Routes.Home.route)
                    }
                )
            }


        }
    }
}