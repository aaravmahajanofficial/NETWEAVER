package com.example.netweaver.ui.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.netweaver.R

@Composable
fun PostCard(
) {

    Surface(
        modifier = Modifier
            .padding(top = 6.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 0.dp, start = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.Top
        ) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.Top)
                            .size(46.dp)
                            .background(color = Color.Blue, shape = CircleShape),
                    )

                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {

                        Text(
                            text = "Deepinder Goyal",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Building Zomato",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {

                            Text(
                                "1w • ", style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onTertiary,
                            )

                            Icon(
                                painter = painterResource(R.drawable.earth),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier.size(12.dp)
                            )

                        }

                    }
                }

                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Icon(
                        imageVector = Icons.Sharp.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Follow",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            Text(
                "If Ipsum is simply dummy text of make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop shares approachable like Aldus PageMaker including versions of Lorem Ipsum.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(-(5).dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.like_count),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                    )
                    Image(
                        painter = painterResource(R.drawable.heart_reaction),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)

                    )
                    Image(
                        painter = painterResource(R.drawable.clap_reaction),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)

                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        "197",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.W400
                        ),
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }

                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "20 comments • 20 reposts",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.W400
                        ),
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }


            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(
                color = Color(0xFFebebeb)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Like, Comment, Repost, Send
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                ReactionButton(id = R.drawable.like, title = "Like")
                ReactionButton(id = R.drawable.comment, title = "Comment")
                ReactionButton(id = R.drawable.repost, title = "Repost")
                ReactionButton(id = R.drawable.send, title = "Send")

            }

            Spacer(modifier = Modifier.height(8.dp))

        }

    }

}

@Composable
fun ReactionButton(id: Int, title: String) {

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.W400
            ),
            color = MaterialTheme.colorScheme.tertiary
        )

    }

}