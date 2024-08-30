package com.example.librarybooksearchapp.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_library")
data class DataLibraryEntity(
    val systemid: String,
    @PrimaryKey val libid: String,
    val formal: String,
    val address: String,
    val post: String,
    val tel: String,
)
