package com.example.netweaver.ui

import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.netweaver.R

@Composable
fun ProfileActivityCard(
    userName: String,
    content: String,
    commentsCount: Long,
    likesCount: Long,
    mediaUrl: String?,
    createdAt: String
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            val annotatedString = buildAnnotatedString {

                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontStyle = MaterialTheme.typography.labelMedium.fontStyle,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) {
                    append(userName)
                }

                append(" posted this • $createdAt")

            }

            Text(
                text = annotatedString,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 18.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (mediaUrl != null) {
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = mediaUrl,
                            contentDescription = "Post Image",
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Text(
                    text = content,
                    maxLines = 3,
                    style = MaterialTheme.typography.labelLarge,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis
                )

            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.like_count),
                        contentDescription = "Likes"
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    "$likesCount •  $commentsCount comments",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiary
                )

            }


            Spacer(modifier = Modifier.height(12.dp))

        }
    }
}
