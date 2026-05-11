package com.example.nammapustaka.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val bookId: Int,

    val studentName: String,

    val rating: Float,

    val review: String
)