package com.example.netweaver.ui.features.jobs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.netweaver.R
import com.example.netweaver.ui.components.AppScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsScreen() {

    AppScaffold(
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
        content = { innerPadding ->
            JobsContent(
                paddingValues = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                )
            )
        }
    )


}

@Composable
private fun JobsContent(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(color = MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 24.dp)
        ) {
            item {
                Text(
                    "Explore Opportunities",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 18.dp)
                )
            }
            items(2) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.surface)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .aspectRatio(3f / 1.5f),
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 14.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Header
                        Header(title = "UI/UX Designer", companyName = "Athena Infomics")

                        // Body
                        Body(
                            location = "Chennai (Remote)",
                            experience = "0-1 yrs",
                            salary = "3-8 Lacs PA"
                        )

                        // Footer
                        Footer(
                            isEasy = false,
                            time = "1hour ago"
                        )

                    }

                }
            }
        }
    }
}

@Composable
private fun Header(
    title: String,
    companyName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clip(RoundedCornerShape(4.dp))
            )

            Column(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Text(
                    companyName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.bookmark),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )

            Icon(
                painterResource(R.drawable.cancel),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )

        }
    }
}


@Composable
private fun Body(
    location: String,
    experience: String,
    salary: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painterResource(R.drawable.location),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onTertiary
            )

            Text(
                location,
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 0.1.sp),
                color = MaterialTheme.colorScheme.onTertiary,
            )


        }


        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painterResource(R.drawable.trip),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onTertiary
            )

            Text(
                experience,
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 0.1.sp),
                color = MaterialTheme.colorScheme.onTertiary,
            )

        }

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painterResource(R.drawable.ruppee),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onTertiary
            )

            Text(
                salary,
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 0.1.sp),
                color = MaterialTheme.colorScheme.onTertiary,
            )

        }

    }
}

@Composable
private fun Footer(
    isEasy: Boolean,
    time: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (!isEasy) {
                Icon(
                    painterResource(R.drawable.link),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.Magenta
                )

                Text(
                    "Direct Apply",
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 0.1.sp,
                        fontWeight = FontWeight.W600
                    ),
                    color = Color.Magenta
                )

            } else {
                Image(
                    painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    "Easy Apply",
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 0.1.sp,
                        fontWeight = FontWeight.W600
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }


        }

        Text(
            time,
            style = MaterialTheme.typography.labelMedium.copy(
                letterSpacing = 0.1.sp
            ),
            color = MaterialTheme.colorScheme.onTertiary,
        )


    }
}