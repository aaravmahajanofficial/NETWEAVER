package com.example.netweaver.ui.features.home

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.netweaver.domain.model.Post
import com.example.netweaver.ui.components.AppScaffold
import com.example.netweaver.ui.features.home.components.PostCard
import com.example.netweaver.ui.model.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppScaffold(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
        content = { innerPadding ->
            HomeContent(
                uiState = uiState,
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
    uiState: UiState<List<Post>>,
    paddingValues: PaddingValues,
) {

    when (uiState) {
        UiState.Loading -> CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )

        is UiState.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.data, key = { post -> post.id }) { post ->
                    PostCard(post = post)
                }
            }
        }

        is UiState.Error -> {
            Log.d("ERROR: UIState", uiState.message.toString())
        }
    }

}