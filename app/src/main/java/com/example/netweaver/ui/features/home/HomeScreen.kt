package com.example.netweaver.ui.features.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.netweaver.R
import com.example.netweaver.ui.components.AppScaffold
import com.example.netweaver.ui.components.BottomItem
import com.example.netweaver.ui.components.CommonBottomBar
import com.example.netweaver.ui.components.CommonTopBar
import com.example.netweaver.ui.components.CustomBarItem
import com.example.netweaver.ui.features.home.components.PostCard

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun HomeScreen(
) {

    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

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

    AppScaffold(
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
        content = { innerPadding ->
            HomeContent(
                paddingValues = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                )
            )

        }
    )
}

@Composable
private fun HomeContent(
    paddingValues: PaddingValues,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {

            item {
                PostCard()
            }
        }
    }


}