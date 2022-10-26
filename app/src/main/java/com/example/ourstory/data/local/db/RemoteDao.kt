package com.example.ourstory.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ourstory.data.local.entity.RemoteKeyData

@Dao
interface RemoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(remoteKey: List<RemoteKeyData>)

    @Query("SELECT * FROM remote_key_table WHERE id = :id")
    suspend fun getRemoteKeyById(id: String?): RemoteKeyData?

    @Query("DELETE FROM remote_key_table")
    suspend fun deleteRemoteKey()
}