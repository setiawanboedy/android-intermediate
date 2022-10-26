package com.example.ourstory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key_table")
data class RemoteKeyData(
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
