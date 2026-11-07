package com.example.interviewchallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.interviewchallenge.data.model.AppInfo
import com.example.interviewchallenge.data.model.PermissionInfo
import com.example.interviewchallenge.repository.AppDatabaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.isNotBlank
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class AppDatabaseViewModel @Inject constructor(
    private val appDataBaseRepo: AppDatabaseRepo
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("")
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _systemFilter = MutableStateFlow<Boolean?>(null) // null = all, true = system, false = user
    val systemFilter = _systemFilter.asStateFlow()

    fun onSystemFilterSelected(isSystem: Boolean?) {
        _systemFilter.value = isSystem
    }

    fun onFilterSelected(filter: String) {
        _selectedFilter.value = filter
    }

    val searchResultApps: StateFlow<List<AppInfo>> = combine(
        _searchQuery.debounce(200.milliseconds),
        _selectedFilter,
        _systemFilter
    ) { query, riskFilter, sysFilter ->
        Triple(query, riskFilter, sysFilter)
    }.flatMapLatest { (query, riskFilter, sysFilter) ->
        if (query.isNotBlank()) {
            appDataBaseRepo.searchAppsByKeyword(query)
        } else {
            appDataBaseRepo.getAppsFiltered(riskFilter, sysFilter)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _allPermissions = MutableStateFlow<List<PermissionInfo>>(emptyList())
    val allPermissions = _allPermissions.asStateFlow()

    private val _appByPackage = MutableStateFlow<AppInfo?>(null)
    val appByPackage = _appByPackage.asStateFlow()

    private val _safeAppsCount = MutableStateFlow(0)
    val safeAppsCount = _safeAppsCount.asStateFlow()

    private val _mediumRiskAppsCount = MutableStateFlow(0)
    val mediumRiskAppsCount = _mediumRiskAppsCount.asStateFlow()

    private val _highRiskAppsCount = MutableStateFlow(0)
    val highRiskAppsCount = _highRiskAppsCount.asStateFlow()

    private var countsJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }





    fun getAppByPackage(pkg: String) {
        viewModelScope.launch {
            appDataBaseRepo.getAppByPackage(pkg).collectLatest {
                _appByPackage.emit(it)
            }
        }
    }

    fun loadCounts() {
        countsJob?.cancel()
        countsJob = viewModelScope.launch {
            launch {
                appDataBaseRepo.countSafeApps().collectLatest {
                    _safeAppsCount.emit(it)
                }
            }
            launch {
                appDataBaseRepo.countMediumRiskApps().collectLatest {
                    _mediumRiskAppsCount.emit(it)
                }
            }
            launch {
                appDataBaseRepo.countHighRiskApps().collectLatest {
                    _highRiskAppsCount.emit(it)
                }
            }
        }
    }

    fun getPermissions(pkg: String) {
        viewModelScope.launch {
            appDataBaseRepo.getPermissions(pkg).collectLatest {
                _allPermissions.emit(it)
            }
        }
    }

    fun deleteAppByPackage(pkg: String) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataBaseRepo.deleteAppByPackage(pkg)
        }
    }
}
