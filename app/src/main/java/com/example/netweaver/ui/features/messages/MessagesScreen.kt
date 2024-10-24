package com.example.netweaver.ui.features.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.netweaver.R
import com.example.netweaver.ui.components.AppScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen() {
    AppScaffold(
        showBack = true,
        showBottomAppBar = false,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(5.dp),
                modifier = Modifier
                    .padding(bottom = 32.dp)
            ) {

                Icon(
                    painter = painterResource(R.drawable.create_new_message),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )

            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
        content = { innerPadding ->
            MessagesContent(
                paddingValues = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                )
            )
        }
    )
}

@Composable
private fun MessagesContent(paddingValues: PaddingValues) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxSize()
        ) {
            items(10) {

                Row(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(top = 12.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .size(48.dp)
                                .background(
                                    color = Color.Blue.copy(alpha = 0.7f),
                                    shape = CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    "Deepinder Goyal",
                                    style = MaterialTheme.typography.titleSmall,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Text(
                                    "Now",
                                    maxLines = 1,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                            }

                            Text(
                                "Sponsored • Unlock new Opportunities in the country UK, USA",
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(end = 12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))


                            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                        }


                    }

                }

            }
        }
    }

}