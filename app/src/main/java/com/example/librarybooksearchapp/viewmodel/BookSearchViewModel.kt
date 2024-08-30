package com.example.librarybooksearchapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.librarybooksearchapp.model.database.DataBookEntity
import com.example.librarybooksearchapp.model.database.DataRentalStatus
import com.example.librarybooksearchapp.model.repository.GetBookDataRepository
import com.example.librarybooksearchapp.model.repository.GetRentalStatusRepository
import com.example.librarybooksearchapp.model.repository.MyBookRepository
import com.example.librarybooksearchapp.model.repository.MyLibraryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BookSearchViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val _getBookSearchRepository = GetBookDataRepository()
    private val _myLibraryRepository = MyLibraryRepository(application)
    private val _getRentalStatusRepository = GetRentalStatusRepository()
    private val _myBookRepository = MyBookRepository(application)

    val searchBarWord = MutableLiveData<String>()
    var q: String = ""
    private var startIndex = 0
    val bookList = MutableLiveData<List<DataBookEntity>>()
    val searchStatus = MutableLiveData<Boolean>()
    var selectBook = DataBookEntity("", "", "", "", "", "", "")
    val rentalStatusList = mutableListOf<DataRentalStatus>()

    // 検索結果をクリアするメソッド
    fun clearBookList() {
        startIndex = 0
        viewModelScope.launch {
            bookList.value = listOf()
        }
    }

    // 検索するメソッド
    fun addBookList(q: String) {
        val oldList: List<DataBookEntity> = bookList.value!!
        viewModelScope.launch {
            searchStatus.value = true
            for (i in 0..19) {
                bookList.value =
                    _getBookSearchRepository.getBookList(q, startIndex, bookList.value!!)
                startIndex += 1
                if (bookList.value!!.size - oldList.size >= 10) break
            }
            searchStatus.value = false
        }
    }

    // カーリルへのリンクを生成するメソッド
    fun createCalilLink(): Uri =
        Uri
            .parse("https://calil.jp/book")
            .buildUpon()
            .appendPath(selectBook.isbn)
            .build()

    // 蔵書を検索するメソッド
    fun getRentalStatus(): Job {
        val job =
            viewModelScope.launch {
                rentalStatusList.clear()
                val myLibraryList = _myLibraryRepository.selectMyLibrary()
                for (i in myLibraryList.indices) {
                    rentalStatusList.add(
                        _getRentalStatusRepository.getRentalStatus(
                            selectBook.isbn,
                            myLibraryList[i],
                        ),
                    )
                }
            }
        return job
    }

    // マイ本棚へ追加するメソッド
    suspend fun insertMyBook(dataBookEntity: DataBookEntity): Job {
        val job =
            viewModelScope.launch {
                _myBookRepository.insertMyBook(
                    DataBookEntity(
                        dataBookEntity.title,
                        dataBookEntity.authors,
                        dataBookEntity.publishedDate,
                        dataBookEntity.description,
                        dataBookEntity.isbn,
                        dataBookEntity.thumbnail,
                        LocalDateTime.now().toString(),
                    ),
                )
            }
        return job
    }
}
