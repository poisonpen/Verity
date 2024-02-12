package com.example.chapter.database

import androidx.room.*
import com.example.chapter.Chapter
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapter")
    fun getChapters(): Flow<List<Chapter>>

    @Query("SELECT * FROM chapter WHERE id=(:id)")
    suspend fun getChapter(id: UUID): Chapter

    @Update
    suspend fun updateChapter(chapter: Chapter)

    @Insert
    suspend fun addChapter(chapter: Chapter)

    @Delete
    suspend fun deleteChapter(chapter: Chapter)
}
