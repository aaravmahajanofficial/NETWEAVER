package com.example.netweaver.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.netweaver.ui.features.auth.AuthState
import com.example.netweaver.ui.features.auth.AuthViewModel
import com.example.netweaver.ui.features.auth.ForgotPasswordScreen
import com.example.netweaver.ui.features.auth.login.LoginScreen
import com.example.netweaver.ui.features.auth.register.RegisterScreen
import com.example.netweaver.ui.features.createPost.CreatePostScreen
import com.example.netweaver.ui.features.home.HomeScreen
import com.example.netweaver.ui.features.jobs.JobsScreen
import com.example.netweaver.ui.features.messages.MessagesScreen
import com.example.netweaver.ui.features.mynetwork.MyNetworkScreen
import com.example.netweaver.ui.features.notifications.NotificationsScreen
import com.example.netweaver.ui.features.profile.ProfileScreen

class AppNavigator(private val navController: NavHostController) {

    val controller: NavController
        get() = navController

    fun navigateTo(event: NavigationEvent) {
        when (event) {
            NavigationEvent.NavigationToHome -> navController.navigate(route = Routes.Home.route) {
                popUpTo(route = Routes.Login.route) {
                    inclusive = true
                }
            }

            NavigationEvent.NavigationToMyNetwork -> navController.navigate(route = Routes.Home.route)
            NavigationEvent.NavigationToJobs -> navController.navigate(route = Routes.Jobs.route)
            NavigationEvent.NavigationToNotifications -> navController.navigate(route = Routes.Notifications.route)
            NavigationEvent.NavigationToPost -> navController.navigate(route = Routes.Post.route)
            NavigationEvent.NavigationToMessages -> navController.navigate(route = Routes.Messages.route)
            NavigationEvent.NavigationToProfile -> navController.navigate(route = Routes.Profile.route)
            NavigationEvent.NavigateToLogin -> navController.navigate(route = Routes.Login.route) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        }
    }
}

val LocalAppNavigator = compositionLocalOf<AppNavigator> {
    error("No NavController provided")
}

@Composable
fun NavGraph(
    viewModel: AuthViewModel = hiltViewModel()
) {

    val navController = rememberNavController()
    val appNavigator = remember { AppNavigator(navController) }

    val authState by viewModel.authState.collectAsStateWithLifecycle()

    val startDestination =
        when (authState) {
            is AuthState.Loading -> {
                Routes.Home.route
            }

            is AuthState.Success -> {
                Routes.Home.route
            }

            is AuthState.Error -> {
                Routes.Home.route
            }
        }

    CompositionLocalProvider(LocalAppNavigator provides appNavigator) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
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

            composable(route = Routes.Home.route) { HomeScreen() }
            composable(route = Routes.MyNetwork.route) { MyNetworkScreen() }
            composable(route = Routes.Post.route) { HomeScreen() }
            composable(route = Routes.Notifications.route) { NotificationsScreen() }
            composable(route = Routes.Jobs.route) { JobsScreen() }
            composable(route = Routes.Messages.route) { MessagesScreen() }
            composable(route = Routes.Profile.route) { ProfileScreen() }
            composable(route = Routes.CreatePost.route) {
                CreatePostScreen(
                    onNavigateBack = {
                        navController.navigate(route = Routes.Home.route)
                    }
                )
            }
            composable(route = Routes.Login.route) { LoginScreen() }
            composable(route = Routes.Register.route) { RegisterScreen() }
            composable(route = Routes.ForgotPassword.route) { ForgotPasswordScreen() }
        }
    }
}

sealed class NavigationEvent {
    object NavigationToHome : NavigationEvent()
    object NavigationToMyNetwork : NavigationEvent()
    object NavigationToPost : NavigationEvent()
    object NavigationToNotifications : NavigationEvent()
    object NavigationToJobs : NavigationEvent()
    object NavigationToMessages : NavigationEvent()
    object NavigationToProfile : NavigationEvent()
    object NavigateToLogin : NavigationEvent()
}