package com.example.netweaver.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.netweaver.R
import com.example.netweaver.navigation.LocalAppNavigator
import com.example.netweaver.navigation.Routes

sealed class BottomNavItem(
    @DrawableRes val iconRes: Int,
    @DrawableRes val selectedIconRes: Int,
    val route: String,
    val label: String
) {

    object Home : BottomNavItem(
        iconRes = R.drawable.home,
        selectedIconRes = R.drawable.selected_home,
        route = Routes.Home.route,
        label = "Home"
    )

    object MyNetwork : BottomNavItem(
        iconRes = R.drawable.my_network,
        selectedIconRes = R.drawable.selected_my_network,
        route = Routes.MyNetwork.route,
        label = "My Network"
    )

    object Post : BottomNavItem(
        iconRes = R.drawable.post,
        selectedIconRes = R.drawable.post,
        route = Routes.Home.route,
        label = "Post"
    )

    object Notifications : BottomNavItem(
        iconRes = R.drawable.notifications,
        selectedIconRes = R.drawable.selected_notifications,
        route = Routes.Notifications.route,
        label = "Notifications"
    )

    object Jobs : BottomNavItem(
        iconRes = R.drawable.jobs,
        selectedIconRes = R.drawable.selected_jobs,
        route = Routes.Jobs.route,
        label = "Jobs"
    )

    companion object {
        val items = listOf(Home, MyNetwork, Post, Notifications, Jobs)
    }

    fun navigate(navController: NavController) {
        when (this) {
            is Post -> {}
            else -> {
                navController.navigate(route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Avoid multiple copies of the same destination when
                    // re-selecting the same item
                    launchSingleTop = true
                    // Restore state when re-selecting a previously selected item
                    restoreState = true
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    showBack: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior,
    showBottomAppBar: Boolean = true,
    content: @Composable (PaddingValues) -> Unit,
    floatingActionButton: @Composable (() -> Unit) = {}
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val windowInsets = WindowInsets.systemBars
    val navController = LocalAppNavigator.current.controller

    ModalNavigationDrawer(
        modifier = Modifier.windowInsetsPadding(windowInsets),
        drawerState = drawerState, drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.78f),
                drawerShape = RoundedCornerShape(0.dp),
                drawerTonalElevation = 0.dp,
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 26.dp, vertical = 18.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    shape = CircleShape,
                                    color = colorResource(R.color.black)
                                )
                                .size(72.dp)
                                .clickable {

                                }
                        ) {

                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            "Aarav Mahajan",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            "Pre-Final Year | Thapar Institute of Engineering & Technology, Patiala",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "Panchkula, Haryana, India",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal),
                            color = MaterialTheme.colorScheme.onTertiary
                        )

                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline
                    )

                    Column(
                        modifier = Modifier.padding(horizontal = 26.dp, vertical = 22.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        var annotatedString = buildAnnotatedString {

                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("${350} ")
                            }

                            append("profile viewers")

                        }
                        Text(
                            text = annotatedString,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onTertiary
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ) {
                                append("${16} ")
                            }

                            append("post impressions")
                        }

                        Text(
                            text = annotatedString,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        "Saved Posts",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 26.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(26.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painterResource(R.drawable.settings),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            "Settings",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                    }


                }


            }
        }) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    showBack = showBack,
                    onClick = {}
                )
            },
            bottomBar = {
                if (showBottomAppBar) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 0.dp,
                        modifier = Modifier.height(58.dp),
                    ) {
                        val navBackStackEntry = navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry.value?.destination?.route

                        BottomNavItem.items.forEach { item ->
                            NavigationBarItem(
                                alwaysShowLabel = true,
                                icon = {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.clickable(
                                            indication = null,
                                            interactionSource = remember {
                                                MutableInteractionSource()
                                            }) {
                                            item.navigate(
                                                navController
                                            )
                                        }
                                    ) {
                                        Icon(
                                            painter = if (currentRoute == item.route) painterResource(
                                                item.selectedIconRes
                                            ) else painterResource(item.iconRes),
                                            modifier = Modifier
                                                .size(24.dp),
                                            contentDescription = null,
                                        )

                                        Text(
                                            text = item.label,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.sp
                                            ),
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            textAlign = TextAlign.Center,
                                        )
                                    }

                                },
                                label = {},
                                colors = NavigationBarItemDefaults.colors(
                                    unselectedIconColor = MaterialTheme.colorScheme.onTertiary,
                                    unselectedTextColor = MaterialTheme.colorScheme.onTertiary,
                                    selectedTextColor = MaterialTheme.colorScheme.onBackground,
                                    selectedIconColor = MaterialTheme.colorScheme.onBackground,
                                    indicatorColor = Color.Transparent
                                ),
                                selected = currentRoute == item.route,
                                onClick = {}
                            )

                        }

                    }

                }
            },
            floatingActionButton = {
                floatingActionButton()
            }
        ) { paddingValues ->
            content(paddingValues)

        }
    }

}