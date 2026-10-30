package com.example.interviewchallenge.ui.ui_utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.interviewchallenge.R
import com.example.interviewchallenge.ui.theme.AccentPurpleColor
import com.example.interviewchallenge.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    topBarText: String,
    onClick: () -> Unit = {},
    shouldHaveBackIcon: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.AccentPurpleColor
        ),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (shouldHaveBackIcon) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        modifier = Modifier
                            .padding(end = MaterialTheme.spacing.dp8)
                            .size(24.dp)
                            .align(Alignment.CenterEnd)
                            .clickable { onClick() },
                        contentDescription = "",
                        tint = Color.White
                    )
                }

                Text(
                    text = topBarText,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.align(Alignment.CenterStart),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions()
                }
            }
        },
    )
}

@Preview
@Composable
fun AppTopBarPrev() {
    AppTopBar(stringResource(R.string.topbar_title_scan_apps))
}
