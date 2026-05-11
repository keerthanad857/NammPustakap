package com.example.nammapustaka.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_log")
data class ReadingLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val studentName: String,
    val pagesRead: Int,
    val month: String
)