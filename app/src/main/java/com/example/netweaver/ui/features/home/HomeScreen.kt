package com.example.netweaver.ui.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.netweaver.ui.components.AppScaffold
import com.example.netweaver.ui.features.home.components.PostCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
) {

    AppScaffold(
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