package com.example.ourstory.utils

import androidx.room.Room
import com.example.ourstory.data.local.db.StoryDatabase
import org.robolectric.RuntimeEnvironment

object Helper {

    fun getDatabase(): StoryDatabase {
        return Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.getApplication(),
            StoryDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }
}