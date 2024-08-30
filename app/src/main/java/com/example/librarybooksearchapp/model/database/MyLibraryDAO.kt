package com.example.librarybooksearchapp.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MyLibraryDAO {
    @Query("SELECT * FROM my_library ORDER BY address ASC")
    fun getAll(): LiveData<List<DataLibraryEntity>>

    @Query("SELECT * FROM my_library ORDER BY address ASC")
    suspend fun selectAll(): List<DataLibraryEntity>

//    @Query("SELECT * FROM my_library WHERE libid = :libid")
//    suspend fun findByPK(libid: String): DataLibraryEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dataLibrary: DataLibraryEntity)

    @Delete
    suspend fun delete(dataLibrary: DataLibraryEntity)
}
