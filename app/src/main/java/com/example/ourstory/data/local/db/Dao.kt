package com.example.ourstory.data.local.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ourstory.domain.model.StoryModel

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStory(data: List<StoryModel>)

    @Query("SELECT * FROM story_table")
    fun getAllStory(): PagingSource<Int, StoryModel>

    @Query("DELETE FROM story_table")
    suspend fun deleteAllStory()
}