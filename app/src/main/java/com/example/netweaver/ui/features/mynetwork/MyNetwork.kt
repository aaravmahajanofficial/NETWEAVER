package com.example.netweaver.ui.features.mynetwork

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.netweaver.R
import com.example.netweaver.ui.components.AppScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNetwork() {

    AppScaffold(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
        content = { innerPadding ->
            MyNetworkContent(
                paddingValues = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                )
            )
        }
    )

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyNetworkContent(
    paddingValues: PaddingValues
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.Top
    ) {

        items(1) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(52.dp)
                    .background(color = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 12.dp,
                            end = 12.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Invitations (0)",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Icon(
                        painter = painterResource(R.drawable.right_arrow),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )

                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(52.dp)
                    .background(color = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 12.dp,
                            end = 12.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Manage my network",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Icon(
                        painter = painterResource(R.drawable.right_arrow),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )

                }
            }
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
                    "People you may know from Thapar Institute of Engineering & Technology",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 12.dp, top = 24.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                repeat(4) {
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

                                Image(
                                    painterResource(R.drawable.kevin_wang_cnaescojesi_unsplash),
                                    contentDescription = null,
                                    modifier = Modifier.height(65.dp),
                                    contentScale = ContentScale.Crop
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(start = 12.dp, end = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Column(
                                        modifier = Modifier,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Image(
                                            painterResource(R.drawable.kevin_wang_cnaescojesi_unsplash),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(92.dp)
                                                .offset(y = 16.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )

                                        Text(
                                            "Name",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.W500
                                            ),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            modifier = Modifier.padding(top = 24.dp, bottom = 2.dp)
                                        )

                                        Text(
                                            "Headline",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Normal
                                            ),
                                            color = MaterialTheme.colorScheme.onTertiary,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }

                                    Column(
                                        modifier = Modifier.padding(bottom = 12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        OutlinedButton(
                                            onClick = {},
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.primary
                                            ),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = MaterialTheme.colorScheme.surface,
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(32.dp),
                                        ) {

                                            Text(
                                                "Connect",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Medium
                                                )
                                            )

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


}