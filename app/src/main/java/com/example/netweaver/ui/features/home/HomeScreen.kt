package com.example.netweaver.ui.features.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.netweaver.R
import com.example.netweaver.ui.components.AppScaffold
import com.example.netweaver.ui.components.CommonTopBar
import com.example.netweaver.ui.theme.AppPadding

@SuppressLint("RememberReturnType")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {

    AppScaffold(
        topBar = {
            CommonTopBar(
                showBack = false,
                actions = {},
                leading = {
                    Box(
                        modifier = Modifier
                            .background(
                                shape = CircleShape,
                                color = colorResource(R.color.black)
                            )
                            .size(32.dp)
                            .padding(start = AppPadding.medium)
                    ) {

                    }
                })
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