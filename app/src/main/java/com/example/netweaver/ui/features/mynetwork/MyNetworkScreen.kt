package com.example.netweaver.ui.features.mynetwork

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.netweaver.R
import com.example.netweaver.ui.components.AppScaffold
import com.example.netweaver.ui.components.CustomOutlinedButton
import com.example.netweaver.ui.features.mynetwork.components.ArrowNavigationRow
import com.example.netweaver.ui.features.profile.CustomActionButton
import com.example.netweaver.utils.formatTimestampToRelative

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNetworkScreen(viewModel: MyNetworkViewModel = hiltViewModel()) {

    val uiState by viewModel.myNetworkState.collectAsStateWithLifecycle()
    AppScaffold(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
        content = {
            MyNetworkContent(
                uiState = uiState,
                onAccept = { userId ->
                    viewModel.onEvent(MyNetworkEvent.Accept(userId))
                },
                onConnect = { userId ->
                    viewModel.onEvent(MyNetworkEvent.Connect(userId))
                },
                onIgnore = { userId ->
                    viewModel.onEvent(MyNetworkEvent.Ignore(userId))
                }
            )
        }
    )

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MyNetworkContent(
    uiState: MyNetworkState,
    onAccept: (String) -> Unit,
    onConnect: (String) -> Unit,
    onIgnore: (String) -> Unit,
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {

        item {
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                ArrowNavigationRow(
                    title = "Invitations (${uiState.pendingInvitations?.size})",
                    onClick = {}
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline
                )

                uiState.pendingInvitations?.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Image(
                            painter = if (isSystemInDarkTheme()) painterResource(R.drawable.profile_avatar_dark) else painterResource(
                                R.drawable.profile_avatar
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                item.user?.fullName ?: "Requester Name",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                item.user?.userId ?: "--",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier.padding(end = 12.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                formatTimestampToRelative(item.createdAt.toString()),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onTertiary
                            )


                        }

                        CustomOutlinedButton(
                            icon = painterResource(R.drawable.cancel),
                            buttonSize = 32.dp,
                            borderColor = MaterialTheme.colorScheme.onTertiary,
                            iconColor = MaterialTheme.colorScheme.onTertiary,
                            onClick = {
                                item.user?.userId?.let {
                                    onIgnore(it)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        CustomOutlinedButton(
                            icon = painterResource(R.drawable.tick),
                            buttonSize = 32.dp,
                            borderColor = MaterialTheme.colorScheme.secondary,
                            iconColor = MaterialTheme.colorScheme.secondary,
                            onClick = {
                                item.user?.userId?.let {
                                    onAccept(it)
                                }
                            }
                        )

                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            ArrowNavigationRow(
                title = "Manage my network",
                onClick = {}
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(color = MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    "People you may know",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 12.dp, top = 24.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                uiState.recommendations?.forEach { item ->
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(2) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .weight(1f)
                                    .aspectRatio(0.6f)
                                    .border(
                                        1.5.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clip(shape = RoundedCornerShape(12.dp)),
                            ) {

                                // Profile Background Image
                                if (item.profileImageUrl.isNullOrEmpty()) {
                                    Image(
                                        painter = if (isSystemInDarkTheme()) painterResource(R.drawable.profile_background_dark) else painterResource(
                                            R.drawable.profile_background
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.height(65.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    AsyncImage(
                                        model = item.profileImageUrl,
                                        contentDescription = null,
                                        modifier = Modifier.height(65.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                // Profile Details + Connect button
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Column(
                                        modifier = Modifier,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Image(
                                            painter = if (isSystemInDarkTheme()) painterResource(R.drawable.profile_avatar_dark) else painterResource(
                                                R.drawable.profile_avatar
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(92.dp)
                                                .offset(y = 16.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )

                                        Text(
                                            item.fullName,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            textAlign = TextAlign.Center,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            modifier = Modifier.padding(top = 24.dp, bottom = 1.dp)
                                        )

                                        Text(
                                            item.headline ?: "--",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onTertiary,
                                            textAlign = TextAlign.Center,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(horizontal = 12.dp)
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.padding(bottom = 12.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CustomActionButton(
                                            title = "Connect",
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            contentColor = MaterialTheme.colorScheme.secondary,
                                            onClick = {
                                                onConnect(item.userId)
                                            })
                                    }
                                }

                            }
                        }


                    }
                }


            }

        }

    }


}