package com.example.foodrecipesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SyncInfoDao {

    @Query("SELECT * FROM sync_info WHERE key = :key LIMIT 1")
    suspend fun getSyncInfo(key: String): SyncInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncInfo(syncInfo: SyncInfoEntity)
}