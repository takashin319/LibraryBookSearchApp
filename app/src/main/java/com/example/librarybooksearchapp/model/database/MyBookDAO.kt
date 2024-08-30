package com.example.librarybooksearchapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MyBookDAO {
    @Query("SELECT * FROM my_book ORDER BY addDate DESC")
    fun getAll(): LiveData<List<DataBookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dataBook: DataBookEntity)

    @Delete
    suspend fun delete(dataBook: DataBookEntity)
}
