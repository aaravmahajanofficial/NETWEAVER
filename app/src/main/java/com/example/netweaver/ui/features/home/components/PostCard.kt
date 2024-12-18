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
import androidx.compose.ui.graphics.Color
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
fun PostCard(
    post: Post,
    onLikePost: () -> Unit,
    onUnLikePost: () -> Unit,
    isProcessingReaction: Boolean = false,
    onNavigationEvent: () -> Unit,
) {

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
                            .clickable {
                                onNavigationEvent()
                            }
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
                            .padding(start = 8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {

                        Text(
                            text = post.user?.fullName ?: "User",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.W600,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = post.user?.headline ?: "Headline",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {

                            Text(
                                "${post.createdAt} • ",
                                style = MaterialTheme.typography.labelSmall,
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
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Follow",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
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

                if (post.isLiked) {
                    ReactionButton(
                        iconPainter = painterResource(R.drawable.like_count),
                        labelText = "Like",
                        onReactionClick = onUnLikePost,
                        labelTextColor = MaterialTheme.colorScheme.primary,
                        tintFilter = null,
                        enabled = !isProcessingReaction
                    )
                } else {
                    ReactionButton(
                        iconPainter = painterResource(R.drawable.like),
                        labelText = "Like",
                        onReactionClick = onLikePost,
                        enabled = !isProcessingReaction
                    )
                }
                ReactionButton(
                    iconPainter = painterResource(R.drawable.comment),
                    labelText = "Comment",
                    onReactionClick = {},
                    enabled = !isProcessingReaction
                )
                ReactionButton(
                    iconPainter = painterResource(R.drawable.share),
                    labelText = "Share",
                    onReactionClick = {},
                    enabled = !isProcessingReaction
                )
                ReactionButton(
                    iconPainter = painterResource(R.drawable.save),
                    labelText = "Save",
                    onReactionClick = {},
                    enabled = !isProcessingReaction
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

        }

    }

}

@Composable
fun ReactionButton(
    iconPainter: Painter,
    labelText: String,
    enabled: Boolean,
    onReactionClick: () -> Unit,
    labelTextColor: Color = MaterialTheme.colorScheme.tertiary,
    tintFilter: ColorFilter? = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
) {

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = iconPainter,
            contentDescription = null,
            colorFilter = tintFilter,
            modifier = Modifier
                .size(16.dp)
                .clickable {
                    if (enabled == true) {
                        onReactionClick()
                    }
                }
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            labelText,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.W500),
            color = labelTextColor
        )

    }

}

