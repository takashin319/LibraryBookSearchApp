package com.example.librarybooksearchapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.librarybooksearchapp.model.database.DataBookEntity
import com.example.librarybooksearchapp.model.database.DataRentalStatus
import com.example.librarybooksearchapp.model.repository.GetRentalStatusRepository
import com.example.librarybooksearchapp.model.repository.MyBookRepository
import com.example.librarybooksearchapp.model.repository.MyLibraryRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MyBookViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _myLibraryRepository = MyLibraryRepository(application)
    private val _getRentalStatusRepository = GetRentalStatusRepository()
    private val _myBookRepository = MyBookRepository(application)

    val myBookList: LiveData<List<DataBookEntity>> = _myBookRepository.getMyBook()
    var selectBook = DataBookEntity("", "", "", "", "", "", "")
//    val rentalStatusList = mutableListOf<DataRentalStatus>()

    // ViewModelのイベントを通知するFlow
    private val channelGetRentalStatus =
        Channel<List<DataRentalStatus>>(capacity = Channel.UNLIMITED)
    val eventGetRentalStatus = channelGetRentalStatus.receiveAsFlow()

    // カーリルへのリンクを生成するメソッド
    fun createCalilLink(): Uri =
        Uri
            .parse("https://calil.jp/book")
            .buildUpon()
            .appendPath(selectBook.isbn)
            .build()

    // 蔵書を検索するメソッド
//    fun getRentalStatus(): Job {
//        val job =
//            viewModelScope.launch {
//                rentalStatusList.clear()
//                val myLibraryList = _myLibraryRepository.selectMyLibrary()
//                for (i in myLibraryList.indices) {
//                    rentalStatusList.add(
//                        _getRentalStatusRepository.getRentalStatus(
//                            selectBook.isbn,
//                            myLibraryList[i],
//                        ),
//                    )
//                }
//            }
//        return job
//    }

    fun getRentalStatus() {
        viewModelScope.launch {
            val rentalStatusList = mutableListOf<DataRentalStatus>()
            val myLibraryList = _myLibraryRepository.selectMyLibrary()
            for (i in myLibraryList.indices) {
                rentalStatusList.add(
                    _getRentalStatusRepository.getRentalStatus(
                        selectBook.isbn,
                        myLibraryList[i],
                    ),
                )
            }
            channelGetRentalStatus.send(rentalStatusList)
        }
    }

    // マイ本棚から削除するメソッド
//    suspend fun deleteMyBook(dataBook: DataBookEntity): Job {
//        val job =
//            viewModelScope.launch {
//                _myBookRepository.deleteMyBook(dataBook)
//            }
//        return job
//    }

    suspend fun deleteMyBook(dataBook: DataBookEntity) {
        viewModelScope.launch {
            _myBookRepository.deleteMyBook(dataBook)
        }
    }
}
