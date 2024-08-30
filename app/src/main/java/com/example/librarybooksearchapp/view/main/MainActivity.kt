package com.example.librarybooksearchapp.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.librarybooksearchapp.R
import com.example.librarybooksearchapp.view.booksearch.BookSearchActivity
import com.example.librarybooksearchapp.view.librarysearch.LibrarySearchActivity
import com.example.librarybooksearchapp.view.mybook.MyBookActivity
import com.example.librarybooksearchapp.view.mylibrary.MyLibraryActivity

class MainActivity :
    AppCompatActivity(),
    HomeFragment.ItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onClickBtnLibrarySearch() {
        val intent = Intent(this, LibrarySearchActivity::class.java)
        startActivity(intent)
    }

    override fun onClickBtnBookSearch() {
        val intent = Intent(this, BookSearchActivity::class.java)
        startActivity(intent)
    }

    override fun onClickBtnMyLibrary() {
        val intent = Intent(this, MyLibraryActivity::class.java)
        startActivity(intent)
    }

    override fun onClickBtnMyBook() {
        val intent = Intent(this, MyBookActivity::class.java)
        startActivity(intent)
    }
}
