package com.example.librarybooksearchapp.model.repository

import android.net.Uri
import com.example.librarybooksearchapp.BuildConfig
import com.example.librarybooksearchapp.model.database.DataLibraryEntity
import com.example.librarybooksearchapp.model.database.DataRentalStatus
import kotlinx.coroutines.delay
import org.json.JSONException
import org.json.JSONObject

class GetRentalStatusRepository {
    // カーリル図書館APIを利用するためのAPIキー
    private val apiKey = BuildConfig.calilApiKey
    private val _getJsonDataRepository = GetJsonDataRepository()

    suspend fun getRentalStatus(
        isbn: String,
        myLibrary: DataLibraryEntity,
    ): DataRentalStatus {
        // URIの生成
        var uri =
            Uri
                .parse("https://api.calil.jp/check")
                .buildUpon()
                .appendQueryParameter("appkey", apiKey)
                .appendQueryParameter("isbn", isbn)
                .appendQueryParameter("systemid", myLibrary.systemid)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("callback", "no")
                .build()

        // APIから取得したデータをJSONオブジェクトとして定義
        var libkey = ""
        try {
            do {
                val jsonRentalStatus = _getJsonDataRepository.getJsonData(uri)
                val rentalStatusObj = JSONObject(jsonRentalStatus)
                val boolContinue = rentalStatusObj.getInt("continue") == 1
                if (boolContinue) {
                    val session = rentalStatusObj.getString("session")
                    uri =
                        Uri
                            .parse("https://api.calil.jp/check")
                            .buildUpon()
                            .appendQueryParameter("appkey", apiKey)
                            .appendQueryParameter("session", session)
                            .appendQueryParameter("format", "json")
                            .appendQueryParameter("callback", "no")
                            .build()
                    delay(2000)
                } else {
                    val booksObj = rentalStatusObj.getJSONObject("books")
                    val isbnObj = booksObj.getJSONObject(isbn)
                    val systemidObj = isbnObj.getJSONObject(myLibrary.systemid)
                    val libkeyObj = systemidObj.getJSONObject("libkey")
                    libkey =
                        libkeyObj
                            .toString()
                            .replace(",", "\n")
                            .replace("{", "")
                            .replace("}", "")
                            .replace("\"", "")
                    if (libkey.isNullOrBlank()) libkey = "蔵書なし"
                }
            } while (boolContinue)
        } catch (e: JSONException) {
            libkey = "エラー：蔵書が取得できませんでした"
        }

        return DataRentalStatus(myLibrary.formal, libkey)
    }
}
