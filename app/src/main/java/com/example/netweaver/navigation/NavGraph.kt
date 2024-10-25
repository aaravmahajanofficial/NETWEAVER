package com.example.netweaver.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.netweaver.ui.features.home.HomeScreen
import com.example.netweaver.ui.features.jobs.JobsScreen
import com.example.netweaver.ui.features.mynetwork.MyNetworkScreen
import com.example.netweaver.ui.features.notifications.NotificationsScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Routes.Home.route) {

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


    }
}