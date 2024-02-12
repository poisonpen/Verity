package com.example.chapter.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chapter.Chapter

@Database(entities = [Chapter::class], version = 1)
@TypeConverters(ChapterTypeConverters::class)

abstract class ChapterDatabase : RoomDatabase() {
    abstract fun chapterDao(): ChapterDao
}