package com.example.interviewchallenge.ui.screens.app_details

import android.text.format.Formatter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.interviewchallenge.R
import com.example.interviewchallenge.data.model.AppInfo
import com.example.interviewchallenge.ui.theme.BoxGrayColor
import com.example.interviewchallenge.ui.theme.spacing
import com.example.interviewchallenge.ui.ui_utils.InfoCard
import com.example.interviewchallenge.utils.DigitHelper

@Composable
fun TechnicalDetailsCard(
    appInfo: AppInfo
) {
    val context = LocalContext.current
    val formattedSize = Formatter.formatShortFileSize(context, appInfo.apkSize)

    InfoCard(contentPadding = PaddingValues(0.dp)) {
        DetailRow(
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            label = stringResource(R.string.details_label_package_name),
            value = appInfo.packageName
        )

        HorizontalDivider(
            Modifier,
            DividerDefaults.Thickness,
            color = MaterialTheme.colorScheme.BoxGrayColor
        )

        DetailRow(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = stringResource(R.string.details_label_app_version),
            value = DigitHelper.digitByLang(appInfo.versionName)
        )

        HorizontalDivider(
            Modifier,
            DividerDefaults.Thickness,
            color = MaterialTheme.colorScheme.BoxGrayColor
        )

        DetailRow(
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = stringResource(R.string.details_label_install_date),
            value = DigitHelper.digitByLang(appInfo.installTime)
        )

        HorizontalDivider(
            Modifier,
            DividerDefaults.Thickness,
            color = MaterialTheme.colorScheme.BoxGrayColor
        )

        DetailRow(
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            label = stringResource(R.string.details_label_app_size),
            value = DigitHelper.digitByLang(formattedSize)
        )
    }
}

@Composable
fun DetailRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String? = null,
    valueComposable: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.dp16),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.dp8)
        ) {
            icon()
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
        if (valueComposable != null) {
            valueComposable()
        } else if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
