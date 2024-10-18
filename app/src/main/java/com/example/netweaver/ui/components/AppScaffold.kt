package com.example.netweaver.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.netweaver.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    content: @Composable (PaddingValues) -> Unit,
) {

    val navItems = listOf(
        BottomItem(
            icon = painterResource(R.drawable.home),
            selectedIcon = painterResource(R.drawable.selected_home),
            label = "Home"
        ),
        BottomItem(
            icon = painterResource(R.drawable.my_network),
            selectedIcon = painterResource(R.drawable.selected_my_network),
            label = "My Network"
        ),
        BottomItem(
            icon = painterResource(R.drawable.create),
            selectedIcon = painterResource(R.drawable.create),
            label = "Post"
        ),
        BottomItem(
            icon = painterResource(R.drawable.notifications),
            selectedIcon = painterResource(R.drawable.selected_notifications),
            label = "Notifications"
        ),
        BottomItem(
            icon = painterResource(R.drawable.jobs),
            selectedIcon = painterResource(R.drawable.selected_jobs),
            label = "Jobs"
        )
    )

    var selectedItem by remember { mutableStateOf(navItems[0]) }

    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val windowInsets = WindowInsets.systemBars

    ModalNavigationDrawer(
        modifier = Modifier.windowInsetsPadding(windowInsets),
        drawerState = drawerState, drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.78f),
                drawerShape = RoundedCornerShape(0.dp),
                drawerTonalElevation = 0.dp,
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {

                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start) {
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
                CommonTopBar(
                    scrollBehavior = scrollBehavior,
                    showBack = false,
                    actions = {

                        Icon(
                            painter = painterResource(R.drawable.message),
                            contentDescription = "Message",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )

                    },
                    avatar = {
                        Box(
                            modifier = Modifier
                                .background(
                                    shape = CircleShape,
                                    color = colorResource(R.color.black)
                                )
                                .size(32.dp)
                                .clickable {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }
                        ) {

                        }
                    })
            },
            bottomBar = {
                CommonBottomBar(
                    actions = {
                        navItems.forEach { item ->
                            CustomBarItem(item.copy(isSelected = item == selectedItem), onClick = {
                                selectedItem = item
                            })
                        }
                    }
                )
            },
            floatingActionButton = {}
        ) { paddingValues ->
            content(paddingValues)

        }
    }

}