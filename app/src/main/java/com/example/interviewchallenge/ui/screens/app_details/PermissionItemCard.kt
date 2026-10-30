package com.example.interviewchallenge.ui.screens.app_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.interviewchallenge.R
import com.example.interviewchallenge.data.model.PermissionInfo
import com.example.interviewchallenge.ui.theme.GreenColor
import com.example.interviewchallenge.ui.theme.RedColor
import com.example.interviewchallenge.ui.theme.spacing

@Composable
fun PermissionItemCard(permission: PermissionInfo) {
    val permName = permission.permissionName.substringAfterLast(".")
    val isGranted = permission.granted
    val isDangerous = permission.dangerous

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.dp4),
        shape = RoundedCornerShape(MaterialTheme.spacing.dp12),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.dp12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = permName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = permission.permissionName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp6),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isDangerous) MaterialTheme.colorScheme.RedColor.copy(alpha = 0.15f)
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                        )
                        .padding(horizontal = MaterialTheme.spacing.dp8, vertical = MaterialTheme.spacing.dp4)
                ) {
                    Text(
                        text = if (isDangerous) "حساس" else "عادی",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isDangerous) MaterialTheme.colorScheme.RedColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isGranted) MaterialTheme.colorScheme.RedColor.copy(alpha = 0.15f)
                            else MaterialTheme.colorScheme.GreenColor.copy(alpha = 0.15f)
                        )
                        .padding(horizontal = MaterialTheme.spacing.dp8, vertical = MaterialTheme.spacing.dp4)
                ) {
                    Text(
                        text = stringResource(if (isGranted) R.string.status_enabled else R.string.status_disabled),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isGranted) MaterialTheme.colorScheme.RedColor else MaterialTheme.colorScheme.GreenColor
                    )
                }
            }
        }
    }
}
