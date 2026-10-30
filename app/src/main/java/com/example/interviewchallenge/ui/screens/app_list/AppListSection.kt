package com.example.interviewchallenge.ui.screens.app_list

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.interviewchallenge.R
import com.example.interviewchallenge.data.model.RiskLevels
import com.example.interviewchallenge.ui.theme.AccentPurpleColor
import com.example.interviewchallenge.ui.theme.BackgroundColor
import com.example.interviewchallenge.ui.theme.spacing
import com.example.interviewchallenge.viewmodel.AppDatabaseViewModel

@Composable
fun AppListSection(
    paddingValues: PaddingValues,
    databaseViewModel: AppDatabaseViewModel,
    onAppClick: (String) -> Unit
) {
    val searchQuery by databaseViewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by databaseViewModel.selectedFilter.collectAsStateWithLifecycle()
    val currentApps by databaseViewModel.searchResultApps.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.BackgroundColor)
            .padding(horizontal = MaterialTheme.spacing.dp16)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { databaseViewModel.onSearchQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.dp8),
            placeholder = { Text(stringResource(R.string.placeholder_search_apps)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { databaseViewModel.onSearchQueryChanged("") }) {
                        Icon(Icons.Default.Clear, contentDescription = null)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(MaterialTheme.spacing.dp12)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = MaterialTheme.spacing.dp4)
        ) {
            val filterOptions = listOf(
                "" to stringResource(R.string.filter_all),
                RiskLevels.HIGH.name to stringResource(R.string.filter_high),
                RiskLevels.MEDIUM.name to stringResource(R.string.filter_suspicious),
                RiskLevels.SAFE.name to stringResource(R.string.filter_safe)
            )

            filterOptions.forEach { option ->
                FilterChip(
                    selected = selectedFilter == option.first,
                    onClick = { databaseViewModel.onFilterSelected(option.first) },
                    label = { Text(option.second, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.padding(end = MaterialTheme.spacing.dp8),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.AccentPurpleColor,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.dp8))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = MaterialTheme.spacing.dp24)
        ) {
            items(currentApps, key = { it.packageName }) { app ->
                AppCard(
                    appInfo = app,
                    onClick = { onAppClick(app.packageName) }
                )
            }
        }
    }
}
