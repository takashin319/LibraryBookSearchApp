package com.example.librarybooksearchapp.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.librarybooksearchapp.model.database.AppDatabase
import com.example.librarybooksearchapp.model.database.DataBookEntity

class MyBookRepository(
    application: Application,
) {
    private val _db: AppDatabase = AppDatabase.getDatabase(application)

    fun getMyBook(): LiveData<List<DataBookEntity>> {
        val myBookDAO = _db.createMyBookDAO()
        return myBookDAO.getAll()
    }

    suspend fun insertMyBook(dataBook: DataBookEntity) {
        val myBookDAO = _db.createMyBookDAO()
        return myBookDAO.insert(dataBook)
    }

    suspend fun deleteMyBook(dataBook: DataBookEntity) {
        val myBookDAO = _db.createMyBookDAO()
        return myBookDAO.delete(dataBook)
    }
}
