package com.example.netweaver.ui.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.netweaver.R
import com.example.netweaver.domain.model.Post
import com.example.netweaver.utils.ExpandableText

@Composable
fun PostCard(post: Post, onLikePost: () -> Unit) {

    Surface(
        modifier = Modifier
            .padding(top = 8.dp)
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
                            .clip(shape = CircleShape)
                    ) {
                        AsyncImage(
                            model = post.user?.profileImageUrl,
                            contentDescription = "${post.user?.fullName} Profile Picture",
                            contentScale = ContentScale.Fit
                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, end = 16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {

                        Text(
                            text = post.user?.fullName ?: "User",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W600),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = post.user?.headline ?: "Headline",
                            style = MaterialTheme.typography.labelMedium,
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
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(12.dp)
                            )

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
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Follow",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.W500
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )

                    }
                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            ExpandableText(
                text = post.content
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
                        "${post.likesCount} comments • 20 reposts",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.W400
                        ),
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }


            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Like, Comment, Repost, Send
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                ReactionButton(
                    icon = painterResource(R.drawable.like),
                    pressedIcon = painterResource(R.drawable.like_count),
                    title = "Like",
                    onClick = onLikePost,
                    isPressed = post.isLiked
                )
                ReactionButton(
                    icon = painterResource(R.drawable.comment),
                    title = "Comment",
                    onClick = {})
                ReactionButton(
                    icon = painterResource(R.drawable.share),
                    title = "Share",
                    onClick = {})
                ReactionButton(
                    icon = painterResource(R.drawable.save),
                    title = "Save",
                    onClick = {})

            }

            Spacer(modifier = Modifier.height(8.dp))

        }

    }

}

@Composable
fun ReactionButton(
    icon: Painter,
    pressedIcon: Painter? = null,
    title: String,
    onClick: () -> Unit,
    isPressed: Boolean = false
) {

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = if (isPressed == true) pressedIcon!! else icon,
            contentDescription = null,
            colorFilter = if (isPressed == true) null else ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
            modifier = Modifier
                .size(16.dp)
                .clickable {
                    onClick()
                }
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            title,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W500),
            color = if (isPressed == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
        )

    }

}

