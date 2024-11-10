package com.example.netweaver.ui.features.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.netweaver.R
import com.example.netweaver.domain.model.Education
import com.example.netweaver.domain.model.Experience
import com.example.netweaver.ui.components.AppScaffold
import com.example.netweaver.utils.ExpandableText
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val uiState by viewModel.profileUiState.collectAsStateWithLifecycle()

    AppScaffold(
        showBack = true,
        showBottomAppBar = false,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
        content = { innerPadding ->
            ProfileContent(
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
private fun ProfileContent(
    uiState: ProfileState,
    paddingValues: PaddingValues
) {

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.Start
    ) {

        // Header
        item {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface
                    )
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile_background),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Avatar
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painterResource(R.drawable.profile_avatar),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .background(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.surface
                                )
                                .size(120.dp)
                                .padding(4.dp)
                                .clip(shape = CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = uiState.user?.fullName ?: "Full Name",
                        modifier = Modifier.padding(start = 12.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = uiState.user?.headline ?: "--",
                        modifier = Modifier.padding(start = 12.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (!uiState.experience.isNullOrEmpty()) {
                        Text(
                            text = uiState.experience[0].companyName,
                            modifier = Modifier.padding(start = 12.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(2.dp))
                    }

                    Text(
                        text = uiState.user?.location ?: "Location",
                        modifier = Modifier.padding(start = 12.dp),
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "${0} connections",
                        modifier = Modifier.padding(start = 12.dp),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        shape = CircleShape,
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondary
                        ),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier
                            .height(34.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        onClick = {},
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            "Enhance profile",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                        )

                    }

                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

        }

        // About
        if (!uiState.user?.about.isNullOrBlank()) {
            item {

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "About",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                painter = painterResource(R.drawable.pencil),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        ExpandableText(
                            text = uiState.user.about,
                            maxLines = 4
                        )
                    }
                }


            }
        }

        // Activity
        item {
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface)
            ) {

                Column(
                    modifier = Modifier
                        .padding(12.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                "Activity",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.W600,
                                    fontSize = 20.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "${0} followers",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.spacedBy(22.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            TextButton(
                                shape = CircleShape,
                                border = BorderStroke(
                                    width = 1.25.dp,
                                    color = MaterialTheme.colorScheme.secondary
                                ),
                                colors = ButtonDefaults.textButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.secondary
                                ),
                                modifier = Modifier
                                    .height(32.dp)
                                    .weight(1f)
                                    .padding(start = 72.dp),
                                onClick = {},
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    "Create a post",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                )

                            }

                            Icon(
                                painter = painterResource(R.drawable.pencil),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ShowAllBanner(title = "Show all posts")


                }


            }
        }

        if (!uiState.experience.isNullOrEmpty()) {

            item {
                Spacer(modifier = Modifier.height(8.dp))
                CustomCard(title = "Experience", content = uiState.experience)
            }

        }

        if (!uiState.education.isNullOrEmpty()) {

            item {
                Spacer(modifier = Modifier.height(8.dp))
                CustomCard(title = "Education", content = uiState.education)
            }

        }

    }
}

@Composable
private fun CustomCard(title: String, content: List<Any>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.W600,
                        fontSize = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(24.dp)
                    )


                    Icon(
                        painter = painterResource(R.drawable.pencil),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            when (title) {
                "Experience" -> {
                    content.forEach { item ->
                        (item as Experience).let { experience ->
                            ContentCard(
                                title = experience.companyName,
                                subTitle = experience.position,
                                startDate = "${experience.startDate.toLocalDateTime(TimeZone.currentSystemDefault()).month} ${
                                    experience.startDate.toLocalDateTime(
                                        TimeZone.currentSystemDefault()
                                    ).year
                                }",
                                endDate = "${experience.endDate?.toLocalDateTime(TimeZone.currentSystemDefault())?.month} ${
                                    experience.endDate?.toLocalDateTime(
                                        TimeZone.currentSystemDefault()
                                    )?.year
                                }"
                            )
                        }
                    }
                }

                "Education" -> {
                    content.forEach { item ->
                        (item as Education).let { education ->
                            ContentCard(
                                title = education.school,
                                subTitle = "${education.degree}, ${education.field}",
                                startDate = "${education.startDate.toLocalDateTime(TimeZone.currentSystemDefault()).month} ${
                                    education.startDate.toLocalDateTime(
                                        TimeZone.currentSystemDefault()
                                    ).year
                                }",
                                endDate = "${education.endDate?.toLocalDateTime(TimeZone.currentSystemDefault())?.month} ${
                                    education.endDate?.toLocalDateTime(
                                        TimeZone.currentSystemDefault()
                                    )?.year
                                }",
                            )
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(4.dp))

            ShowAllBanner(title = buildString {
                append("Show all ")
                append(content.size)
                append(" ")
                append(title)
                if (content.size != 1) append("s")
            })

        }
    }
}

@Composable
private fun ShowAllBanner(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onTertiary
        )

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            painter = painterResource(R.drawable.right_arrow),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onTertiary,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun ContentCard(
    title: String,
    subTitle: String,
    startDate: String,
    endDate: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = Color.Red,
                    )
                    .clip(RoundedCornerShape(4.dp))
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    subTitle,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    "$startDate - $endDate", style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiary
                )

                Spacer(modifier = Modifier.height(18.dp))


            }


        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline)

    }


}