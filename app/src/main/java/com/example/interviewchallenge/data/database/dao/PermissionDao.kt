package com.example.interviewchallenge.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.interviewchallenge.data.model.PermissionInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface PermissionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<PermissionInfo>)

    @Query("SELECT * FROM permissions WHERE packageName=:pkg")
    fun getPermissions(pkg: String): Flow<List<PermissionInfo>>

    @Query("DELETE FROM permissions WHERE packageName = :pkg")
    suspend fun deleteByPackage(pkg: String)
}
