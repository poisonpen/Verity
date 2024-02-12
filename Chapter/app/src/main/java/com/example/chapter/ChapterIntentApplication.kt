package com.example.chapter

import android.app.Application

class ChapterIntentApplication : Application() {
    override  fun onCreate() {
        super.onCreate()
        ChapterRepository.initialize(this)
    }
}