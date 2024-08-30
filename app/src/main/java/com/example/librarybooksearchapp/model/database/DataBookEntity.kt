package com.example.librarybooksearchapp.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_book")
data class DataBookEntity(
    val title: String,
    val authors: String,
    val publishedDate: String,
    val description: String,
    @PrimaryKey val isbn: String,
    val thumbnail: String,
    val addDate: String,
)
