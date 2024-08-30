package com.example.librarybooksearchapp.model.repository

import android.net.Uri
import com.example.librarybooksearchapp.BuildConfig
import com.example.librarybooksearchapp.model.database.DataLibraryEntity
import org.json.JSONArray
import org.json.JSONException

class GetLibraryDataRepository {
    // カーリル図書館APIを利用するためのAPIキー
    private val apiKey = BuildConfig.calilApiKey
    private val _getJsonDataRepository = GetJsonDataRepository()

    suspend fun getLibraryList(
        pref: String,
        city: String,
    ): List<DataLibraryEntity> {
        // URIの生成
        val uri =
            Uri
                .parse("https://api.calil.jp/library")
                .buildUpon()
                .appendQueryParameter("appkey", apiKey)
                .appendQueryParameter("pref", pref)
                .appendQueryParameter("city", city)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("callback", "")
                .build()
        // 取得したデータを格納するリスト
        val libraryList = mutableListOf<DataLibraryEntity>()
        // APIから取得したデータをJSONオブジェクトとして定義
        val jsonLibraryData = _getJsonDataRepository.getJsonData(uri)
        try {
            val jsonArray = JSONArray(jsonLibraryData)
            for (i in 0 until jsonArray.length()) {
                val libraryObj = jsonArray.getJSONObject(i)
                val systemid = libraryObj.getString("systemid")
                val libid = libraryObj.getString("libid")
                val formal = libraryObj.getString("formal")
                val address = libraryObj.getString("address")
                val post = libraryObj.getString("post")
                val tel = libraryObj.getString("tel")
                libraryList.add(DataLibraryEntity(systemid, libid, formal, address, post, tel))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return libraryList
    }
}
