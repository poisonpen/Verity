package com.example.chapter

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Chapter (
    @PrimaryKey
    val id: UUID,
    val strangeText: String,
    val translationText: String,
    val language: String,
    val comment: String,
    val lastUpdated: Date
)

// PUSH TO GITHUB //