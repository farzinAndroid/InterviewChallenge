package com.example.interviewchallenge.ui.screens.app_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.interviewchallenge.R
import com.example.interviewchallenge.ui.theme.RedColor
import com.example.interviewchallenge.ui.theme.spacing

@Composable
fun AppDetailsBottomBar(
    isSystemApp: Boolean = false,
    onInfoClicked: () -> Unit,
    onUninstallClicked: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.dp16),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onInfoClicked,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(MaterialTheme.spacing.dp16)
            ) {
                Icon(Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                Text(
                    text = stringResource(R.string.button_app_info),
                    fontWeight = FontWeight.Bold
                )
            }

            if (!isSystemApp) {
                Button(
                    onClick = onUninstallClicked,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(MaterialTheme.spacing.dp16),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.RedColor,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                    Text(
                        text = stringResource(R.string.button_uninstall_app),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
