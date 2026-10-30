package com.example.interviewchallenge.repository

import com.example.interviewchallenge.data.database.dao.AppDao
import com.example.interviewchallenge.data.database.dao.PermissionDao
import javax.inject.Inject

class AppDatabaseRepo @Inject constructor(
    private val appDao: AppDao,
    private val permissionDao: PermissionDao
) {

    fun getAppByPackage(pkg: String) = appDao.getByPackage(pkg)

    fun getAppsByRiskLevel(riskLevel: String = "") = appDao.getAppsByRiskLevel(riskLevel)
    fun searchAppsByKeyword(keyword: String) = appDao.searchAppsByKeyword(keyword)

    fun countSafeApps() = appDao.countSafeApps()
    fun countMediumRiskApps() = appDao.countMediumRiskApps()
    fun countHighRiskApps() = appDao.countHighRiskApps()

    suspend fun deleteAppByPackage(pkg: String) {
        appDao.deleteByPackage(pkg)
        permissionDao.deleteByPackage(pkg)
    }

    fun getPermissions(pkg: String) = permissionDao.getPermissions(pkg)
}
