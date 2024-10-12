package com.example.netweaver.ui.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.netweaver.ui.components.AppScaffold
import com.example.netweaver.ui.components.CommonTopBar
import com.example.netweaver.ui.theme.AppPadding

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {

    AppScaffold(
        topBar = {
            CommonTopBar(title = "Home", showBack = false)
        },
        content = { innerPadding ->
            HomeContent(
                paddingValues = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                    start = AppPadding.medium,
                    end = AppPadding.medium
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

    }

}

//@Preview(showBackground = true)
//@Composable
//fun Test() {
//    NETWEAVERTheme {
//        HomeScreen()
//    }
//}