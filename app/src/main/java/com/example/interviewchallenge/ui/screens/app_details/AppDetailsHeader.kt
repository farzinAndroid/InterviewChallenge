package com.example.interviewchallenge.ui.screens.app_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun AppHeaderSection(
    appInfo: AppInfo
) {
    val context = LocalContext.current
    val iconBitmap = remember(appInfo.packageName) {
        try {
            context.packageManager.getApplicationIcon(appInfo.packageName).toBitmap()
        } catch (_: Exception) {
            null
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconModifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(MaterialTheme.spacing.dp16))
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

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = MaterialTheme.spacing.dp16),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = appInfo.appName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.BlackWhiteColor
            )
            Text(
                text = appInfo.packageName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp8))
            RiskBadge(appInfo.riskLevel)
        }
    }
}

@Composable
fun RiskBadge(riskLevel: String) {
    val riskColor = when (riskLevel) {
        RiskLevels.HIGH.name -> MaterialTheme.colorScheme.RedColor
        RiskLevels.MEDIUM.name -> MaterialTheme.colorScheme.YellowColor
        else -> MaterialTheme.colorScheme.GreenColor
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(riskColor.copy(alpha = 0.15f))
            .padding(horizontal = MaterialTheme.spacing.dp8, vertical = MaterialTheme.spacing.dp4)
    ) {
        Text(
            text = stringResource(R.string.format_risk_level, stringResource(RiskLevels.toPersian(riskLevel))),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = riskColor
        )
    }
}
