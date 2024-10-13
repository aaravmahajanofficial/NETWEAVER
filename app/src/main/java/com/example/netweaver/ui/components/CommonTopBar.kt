package com.example.netweaver.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.netweaver.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(
    title: String? = null,
    showBack: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
    avatar: @Composable (() -> Unit) = {}
) {

    var searchQuery by remember { mutableStateOf("") }

    CenterAlignedTopAppBar(
        title = {
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayLarge
                )
            } else {

                Box(
                    modifier = Modifier.padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 2.dp,
                        bottom = 2.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .height(32.dp)
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painterResource(R.drawable.search),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )

                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 4.dp),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        "Search",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onTertiary
                                    )
                                }
                                innerTextField()
                            }
                        )

                    }
                }


            }

        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = {}) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
                }
            } else {
                Box(modifier = Modifier.padding(start = 12.dp)) {
                    avatar()
                }
            }
        },

        actions = {
            Row(
                modifier = Modifier.padding(end = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                content = actions
            )
        },
    )

}