package com.example.interviewchallenge.ui.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.interviewchallenge.R
import com.example.interviewchallenge.ui.navigation.Screens
import com.example.interviewchallenge.ui.theme.AccentPurpleColor
import com.example.interviewchallenge.ui.theme.BackgroundColor
import com.example.interviewchallenge.ui.theme.GreenColor
import com.example.interviewchallenge.ui.theme.RedColor
import com.example.interviewchallenge.ui.theme.YellowColor
import com.example.interviewchallenge.ui.theme.spacing
import com.example.interviewchallenge.ui.ui_utils.ScanPageState
import com.example.interviewchallenge.utils.DigitHelper
import com.example.interviewchallenge.viewmodel.AppDatabaseViewModel
import com.example.interviewchallenge.viewmodel.ScannerViewModel

@Composable
fun HomeScreen(
    scannerViewModel: ScannerViewModel,
    databaseViewModel: AppDatabaseViewModel,
    navController: NavController,
    paddingValues: PaddingValues = PaddingValues()
) {
    val scanState by scannerViewModel.scanState.collectAsStateWithLifecycle()
    val totalCount by scannerViewModel.totalCount.collectAsStateWithLifecycle()
    val scannedCount by scannerViewModel.scannedCount.collectAsStateWithLifecycle()
    val currentAppName by scannerViewModel.appName.collectAsStateWithLifecycle()
    val currentIconBitmap by scannerViewModel.iconBitmap.collectAsStateWithLifecycle()

    val highRiskCount by databaseViewModel.highRiskAppsCount.collectAsStateWithLifecycle()
    val mediumRiskCount by databaseViewModel.mediumRiskAppsCount.collectAsStateWithLifecycle()
    val safeCount by databaseViewModel.safeAppsCount.collectAsStateWithLifecycle()

    LaunchedEffect(scanState) {
        if (scanState == ScanPageState.SCAN_COMPLETE) {
            databaseViewModel.loadCounts()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.BackgroundColor)
            .padding(MaterialTheme.spacing.dp20),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.home_title_security_scanner),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.AccentPurpleColor
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp8))
            Text(
                text = stringResource(R.string.home_subtitle_risk_analysis),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = scanState,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = ""
            ) { state ->
                when (state) {
                    ScanPageState.IDLE -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(MaterialTheme.spacing.dp16)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                modifier = Modifier.size(96.dp),
                                tint = MaterialTheme.colorScheme.AccentPurpleColor
                            )
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp20))
                            Text(
                                text = stringResource(R.string.landing_instruction_tap_to_start),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    ScanPageState.SCANNING -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(MaterialTheme.spacing.dp20),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(MaterialTheme.spacing.dp20),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (currentIconBitmap != null) {
                                    Image(
                                        bitmap = currentIconBitmap!!.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(R.drawable.ic_launcher_background),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                }

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp12))

                                Text(
                                    text = currentAppName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))

                                val progress = if (totalCount > 0) scannedCount.toFloat() / totalCount else 0f
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(MaterialTheme.spacing.dp12)
                                        .clip(RoundedCornerShape(6.dp)),
                                    color = MaterialTheme.colorScheme.AccentPurpleColor
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp12))

                                val scannedFa = DigitHelper.digitByLang(scannedCount.toString())
                                val totalFa = DigitHelper.digitByLang(totalCount.toString())
                                Text(
                                    text = stringResource(R.string.scan_progress_format, scannedFa, totalFa),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp16))

                                OutlinedButton(
                                    onClick = { scannerViewModel.cancelScanJob() },
                                    shape = RoundedCornerShape(MaterialTheme.spacing.dp12)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.RedColor
                                    )
                                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                                    Text(
                                        text = stringResource(R.string.button_cancel_scan),
                                        color = MaterialTheme.colorScheme.RedColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    ScanPageState.SCAN_COMPLETE -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(MaterialTheme.spacing.dp20),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(MaterialTheme.spacing.dp20),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.home_scan_results),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.AccentPurpleColor
                                )
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp20))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    SummaryItem(
                                        label = stringResource(R.string.summary_total),
                                        count = totalCount,
                                        color = MaterialTheme.colorScheme.AccentPurpleColor
                                    )
                                    SummaryItem(
                                        label = stringResource(R.string.summary_high_risk),
                                        count = highRiskCount,
                                        color = MaterialTheme.colorScheme.RedColor
                                    )
                                    SummaryItem(
                                        label = stringResource(R.string.summary_suspicious),
                                        count = mediumRiskCount,
                                        color = MaterialTheme.colorScheme.YellowColor
                                    )
                                    SummaryItem(
                                        label = stringResource(R.string.summary_safe),
                                        count = safeCount,
                                        color = MaterialTheme.colorScheme.GreenColor
                                    )
                                }

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp24))

                                Button(
                                    onClick = {
                                        navController.navigate(Screens.AppList)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(MaterialTheme.spacing.dp12),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.AccentPurpleColor
                                    )
                                ) {
                                    Text(
                                        text = stringResource(R.string.button_view_app_list),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.dp24)
                .navigationBarsPadding()
        ) {
            when (scanState) {
                ScanPageState.IDLE -> {
                    Button(
                        onClick = {
                            scannerViewModel.resetScanState()
                            scannerViewModel.startScan()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(MaterialTheme.spacing.dp16),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.AccentPurpleColor
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                        Text(
                            text = stringResource(R.string.button_start_scan),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                ScanPageState.SCANNING -> {
                    Spacer(modifier = Modifier.height(0.dp))
                }

                ScanPageState.SCAN_COMPLETE -> {
                    OutlinedButton(
                        onClick = {
                            scannerViewModel.resetScanState()
                            scannerViewModel.startScan()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(MaterialTheme.spacing.dp16)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.dp8))
                        Text(
                            text = stringResource(R.string.button_rescan),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
