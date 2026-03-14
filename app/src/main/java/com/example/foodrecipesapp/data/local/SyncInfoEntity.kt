package com.example.foodrecipesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_info")
data class SyncInfoEntity(
    @PrimaryKey val key: String,
    val lastSyncTime: Long
)