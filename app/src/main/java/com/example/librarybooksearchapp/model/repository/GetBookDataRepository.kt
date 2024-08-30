package com.example.librarybooksearchapp.model.repository

import android.net.Uri
import com.example.librarybooksearchapp.BuildConfig
import com.example.librarybooksearchapp.model.database.DataBookEntity
import org.json.JSONException
import org.json.JSONObject

class GetBookDataRepository {
    private val apiKey = BuildConfig.googleBooksApiKey
    private val _getJsonDataRepository = GetJsonDataRepository()

    suspend fun getBookList(
        q: String,
        startIndex: Int,
        oldList: List<DataBookEntity>,
    ): List<DataBookEntity> {
        // URIの生成
        val uri =
            Uri
                .parse("https://www.googleapis.com/books/v1/volumes")
                .buildUpon()
                .appendQueryParameter("q", q)
                .appendQueryParameter("startIndex", startIndex.toString())
                .appendQueryParameter("maxResults", "10")
                .appendQueryParameter("key", apiKey)
                .build()
        // 取得したデータを格納するリスト
        val newList = mutableListOf<DataBookEntity>()
        // APIから取得したデータをJSONオブジェクトとして定義
        val jsonBookData = _getJsonDataRepository.getJsonData(uri)
        try {
            val jsonObj = JSONObject(jsonBookData)
            val itemsArray = jsonObj.getJSONArray("items")
            for (i in 0 until itemsArray.length()) {
                // 必要なオブジェクトを抽出
                val itemObj = itemsArray.getJSONObject(i)
                val volumeInfoObj = itemObj.getJSONObject("volumeInfo")
                // 必要な値を抽出（存在しない場合があるためgetXXXではなくoptXXXを使う）
                val title = volumeInfoObj.optString("title", "タイトル不明")
                val publishedDate = volumeInfoObj.optString("publishedDate", "出版日不明")
                val description = volumeInfoObj.optString("description", "")
                // authorsの抽出（存在しない場合JSONExceptionをcatch）
                val authors =
                    try {
                        volumeInfoObj.getJSONArray("authors").join(",").replace("\"", "")
                    } catch (e: JSONException) {
                        "著者不明"
                    }

                // isbn10または13の抽出（存在しない場合JSONExceptionをcatch）
                var isbn = ""
                try {
                    val industryIdentifiersArray = volumeInfoObj.getJSONArray("industryIdentifiers")
                    for (j in 0 until industryIdentifiersArray.length()) {
                        val industryIdentifiersObj = industryIdentifiersArray.getJSONObject(j)
                        val type = industryIdentifiersObj.getString("type")
                        if (type == "ISBN_10") {
                            isbn = industryIdentifiersObj.getString("identifier")
                            break
                        } else if (type == "ISBN_13") {
                            isbn = industryIdentifiersObj.getString("identifier")
                            break
                        } else {
                            isbn = "書籍コード不明"
                        }
                    }
                } catch (e: JSONException) {
                    isbn = "書籍コード不明"
                }
                // thumbnailの抽出（存在しない場合JSONExceptionをcatch）
                val thumbnail =
                    try {
                        val imageLinksObj = volumeInfoObj.getJSONObject("imageLinks")
                        imageLinksObj.getString("thumbnail").replace("http://", "https://")
                    } catch (e: JSONException) {
                        "サムネイル不明"
                    }
                // リストに追加（isbnが不明の場合は除く）
                if (isbn != "書籍コード不明") {
                    newList.add(
                        DataBookEntity(
                            title,
                            authors,
                            publishedDate,
                            description,
                            isbn,
                            thumbnail,
                            "",
                        ),
                    )
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // isbnの重複を削除してリターン
        return (oldList + newList).distinctBy { a -> a.isbn }
    }
}
