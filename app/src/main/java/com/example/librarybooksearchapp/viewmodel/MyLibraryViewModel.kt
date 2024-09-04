package com.example.librarybooksearchapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.librarybooksearchapp.model.database.DataLibraryEntity
import com.example.librarybooksearchapp.model.repository.MyLibraryRepository
import kotlinx.coroutines.launch

class MyLibraryViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _myLibraryRepository = MyLibraryRepository(application)

    val myLibraryList: LiveData<List<DataLibraryEntity>> = _myLibraryRepository.getMyLibrary()
    var selectLibrary = DataLibraryEntity("", "", "", "", "", "")

    // カーリルへのリンクを生成するメソッド
    fun createCalilLink(): Uri =
        Uri
            .parse("https://calil.jp/library")
            .buildUpon()
            .appendPath(selectLibrary.libid)
            .appendPath(selectLibrary.formal)
            .build()

    // マイ図書館から削除するメソッド
//    suspend fun deleteMyLibrary(dataLibraryEntity: DataLibraryEntity): Job {
//        val job =
//            viewModelScope.launch {
//                _myLibraryRepository.deleteMyLibrary(dataLibraryEntity)
//            }
//        return job
//    }

    suspend fun deleteMyLibrary(dataLibraryEntity: DataLibraryEntity) {
        viewModelScope.launch {
            _myLibraryRepository.deleteMyLibrary(dataLibraryEntity)
        }
    }
}
