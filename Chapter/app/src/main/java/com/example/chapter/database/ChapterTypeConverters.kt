package com.example.chapter.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*

class ChapterTypeConverters {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
    @TypeConverter
    fun toDate(millisecondsSinceEpoch: Long): Date {
        return Date(millisecondsSinceEpoch)
    }
}