package com.example.ourstory.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ourstory.data.local.entity.RemoteKeyData
import com.example.ourstory.domain.model.StoryModel

@Database(entities = [StoryModel::class, RemoteKeyData::class], version = 1, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): Dao
    abstract fun remoteKeyDao(): RemoteDao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null

        fun getDatabase(context: Context): StoryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, StoryDatabase::class.java, "story.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
    }
}