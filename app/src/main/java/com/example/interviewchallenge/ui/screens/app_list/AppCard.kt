package com.example.interviewchallenge.ui.screens.app_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.interviewchallenge.R
import com.example.interviewchallenge.data.model.AppInfo
import com.example.interviewchallenge.data.model.RiskLevels
import com.example.interviewchallenge.ui.theme.BlackWhiteColor
import com.example.interviewchallenge.ui.theme.GreenColor
import com.example.interviewchallenge.ui.theme.RedColor
import com.example.interviewchallenge.ui.theme.YellowColor
import com.example.interviewchallenge.ui.theme.spacing

@Composable
fun AppCard(
    appInfo: AppInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val iconBitmap = remember(appInfo.packageName) {
        try {
            context.packageManager.getApplicationIcon(appInfo.packageName).toBitmap()
        } catch (_: Exception) {
            null
        }
    }

    val riskColor = when (appInfo.riskLevel) {
        RiskLevels.HIGH.name -> MaterialTheme.colorScheme.RedColor
        RiskLevels.MEDIUM.name -> MaterialTheme.colorScheme.YellowColor
        else -> MaterialTheme.colorScheme.GreenColor
    }

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.dp4),
        shape = RoundedCornerShape(MaterialTheme.spacing.dp12),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, riskColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconModifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))

            if (iconBitmap != null) {
                Image(
                    bitmap = iconBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = iconModifier
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = iconModifier
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp12))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = appInfo.appName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.BlackWhiteColor
                )
                Text(
                    text = appInfo.packageName,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(riskColor.copy(alpha = 0.15f))
                    .padding(horizontal = MaterialTheme.spacing.dp8, vertical = MaterialTheme.spacing.dp4)
            ) {
                Text(
                    text = stringResource(RiskLevels.toPersian(appInfo.riskLevel)),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = riskColor
                )
            }
        }
    }
}
