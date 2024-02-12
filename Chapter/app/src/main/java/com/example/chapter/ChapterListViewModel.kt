package com.example.chapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class ChapterListViewModel: ViewModel() {
    private val chapterRepository = ChapterRepository.get()
    private val _chapters: MutableStateFlow<List<Chapter>> = MutableStateFlow(emptyList())
    val chapters: StateFlow<List<Chapter>>
        get() = _chapters.asStateFlow()
    init {
        viewModelScope.launch {
            chapterRepository.getChapters().collect{
                _chapters.value = it
            }
        }
    }
    suspend fun addChapter (chapter: Chapter) {
        chapterRepository.addChapter(chapter)
    }
///    suspend fun loadChapters(): Flow<List<Chapter>> {
//        val result = mutableListOf<Chapter>()
//        delay(5000)
//
//        for (i in 0 until 100) {
//            val chapter = Chapter (
//                id = UUID.randomUUID(),
//                strangeText = "Entry #$i",
//                translationText = "",
//                language = "",
//                comment = "",
//                lastUpdated = Date()
//            )
//            result += chapter
//        }
//        return chapterRepository.getChapters()
//    }
}
