package com.example.chapter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class ChapterDetailViewModel(chapterId: UUID) : ViewModel() {
    private val chapterRepository = ChapterRepository.get()
    private val _chapter: MutableStateFlow<Chapter?> = MutableStateFlow(null)
    val chapter: StateFlow<Chapter?> = _chapter.asStateFlow()

    init {
        viewModelScope.launch {
            _chapter.value = chapterRepository.getChapter(chapterId)
        }
    }
    fun updateChapter(onUpdate: (Chapter) -> Chapter){
        _chapter.update {
                oldChapter ->
            oldChapter?.let{onUpdate(it)}
        }
    }

    suspend fun deleteChapter(chapter: Chapter) {
        Log.d("sadasd","asdasdasd")
        chapterRepository.deleteChapter(chapter)
    }

    override fun onCleared() {
        super.onCleared()
        //viewModelScope.launch {
        chapter.value?.let {
            chapterRepository.updateChapter(it)
        }
        // }
    }


}

class ChapterDetailViewModelFactory(
    private val chapterId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChapterDetailViewModel(chapterId) as T
    }
}


