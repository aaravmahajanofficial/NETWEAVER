package com.example.netweaver.ui.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.netweaver.domain.model.Post
import com.example.netweaver.ui.components.AppScaffold
import com.example.netweaver.ui.features.home.components.PostCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {

    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()

    AppScaffold(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
        content = { innerPadding ->
            HomeContent(
                uiState = uiState,
                paddingValues = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                ),
                onLike = { post ->
                    viewModel.onEvent(HomeEvent.LikePost(post))
                },
                onUnLikePost = { post ->
                    viewModel.onEvent(HomeEvent.UnLikePost(post))
                }
            )

        }
    )
}

@Composable
private fun HomeContent(
    uiState: HomeState,
    onLike: (Post) -> Unit,
    onUnLikePost: (Post) -> Unit,
    paddingValues: PaddingValues,
) {

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        uiState.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    uiState.error,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.posts.orEmpty(), key = { post -> post.docId }) { post ->
                    // Creating a function that will call `onButtonClick`
                    // "When the button is clicked, call the onLikePost function and give it the post as input."
                    PostCard(
                        post = post,
                        onLikePost = { onLike(post) },
                        onUnLikePost = { onUnLikePost(post) },
                        isProcessingReaction = post.id in uiState.reactionQueue
                    )
                }
            }
        }
    }

}