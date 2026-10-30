package com.example.interviewchallenge.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.interviewchallenge.data.model.AppInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(app: AppInfo)

    @Query("DELETE FROM apps WHERE packageName = :pkg")
    suspend fun deleteByPackage(pkg: String)

    @Query("SELECT * FROM apps WHERE packageName=:pkg LIMIT 1")
    fun getByPackage(pkg: String): Flow<AppInfo?>

    @Query("""
        SELECT * FROM apps 
        WHERE :riskLevel = '' OR :riskLevel IS NULL OR riskLevel = :riskLevel
        ORDER BY appName ASC
    """)
    fun getAppsByRiskLevel(riskLevel: String = ""): Flow<List<AppInfo>>

    @Query("""
        SELECT * FROM apps 
        WHERE packageName LIKE '%' || :keyword || '%' OR appName LIKE '%' || :keyword || '%'
        ORDER BY appName ASC
    """)
    fun searchAppsByKeyword(keyword: String): Flow<List<AppInfo>>

    @Query("SELECT COUNT(*) FROM apps WHERE isSystem=1")
    fun countSystemApps(): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE isSystem=0")
    fun countUserApps(): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel = 'SAFE'")
    fun countSafeApps(): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel = 'MEDIUM'")
    fun countMediumRiskApps(): Flow<Int>

    @Query("SELECT COUNT(*) FROM apps WHERE riskLevel = 'HIGH'")
    fun countHighRiskApps(): Flow<Int>
}
