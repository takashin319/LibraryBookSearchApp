package com.example.librarybooksearchapp.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.librarybooksearchapp.model.database.AppDatabase
import com.example.librarybooksearchapp.model.database.DataLibraryEntity

class MyLibraryRepository(
    application: Application,
) {
    private val _db: AppDatabase = AppDatabase.getDatabase(application)

    fun getMyLibrary(): LiveData<List<DataLibraryEntity>> {
        val myLibraryDAO = _db.createMyLibraryDAO()
        return myLibraryDAO.getAll()
    }

    suspend fun selectMyLibrary(): List<DataLibraryEntity> {
        val myLibraryDAO = _db.createMyLibraryDAO()
        return myLibraryDAO.selectAll()
    }

//    suspend fun findMyLibrary(libid: String): DataLibraryEntity? {
//        val myLibraryDAO = _db.createMyLibraryDAO()
//        return myLibraryDAO.findByPK(libid)
//    }

    suspend fun insertMyLibrary(dataLibrary: DataLibraryEntity) {
        val myLibraryDAO = _db.createMyLibraryDAO()
        return myLibraryDAO.insert(dataLibrary)
    }

    suspend fun deleteMyLibrary(dataLibrary: DataLibraryEntity) {
        val myLibraryDAO = _db.createMyLibraryDAO()
        return myLibraryDAO.delete(dataLibrary)
    }
}
