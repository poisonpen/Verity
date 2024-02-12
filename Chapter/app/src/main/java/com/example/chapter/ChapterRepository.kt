package com.example.chapter

import android.content.Context
import androidx.room.Room
import com.example.chapter.Chapter
import com.example.chapter.database.ChapterDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "chapter-database"

class ChapterRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope ) {
    private val database: ChapterDatabase = Room

        .databaseBuilder(
            context.applicationContext,
            ChapterDatabase::class.java,
            DATABASE_NAME
        )
        //.createFromAsset(DATABASE_NAME)
        .build()
    fun getChapters(): Flow<List<Chapter>> = database.chapterDao().getChapters()
    suspend fun getChapter(id: UUID): Chapter = database.chapterDao().getChapter(id)
    fun updateChapter(chapter: Chapter) {
        coroutineScope.launch {
            database.chapterDao().updateChapter(chapter)
        }
    }
    suspend fun deleteChapter(chapter: Chapter) {
        database.chapterDao().deleteChapter(chapter)
    }
    suspend fun addChapter(chapter: Chapter) {
        database.chapterDao().addChapter(chapter)
    }
    companion object {
        private var INSTANCE: ChapterRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null)
                INSTANCE = ChapterRepository(context)
        }
        fun get(): ChapterRepository {
            return INSTANCE ?: throw IllegalStateException("ChapterRepository must be initialized")
        }
    }
}