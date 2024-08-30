package com.example.librarybooksearchapp.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.librarybooksearchapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // FragmentのイベントをActivityで受け取るためのリスナー
    interface ItemClickListener {
        fun onClickBtnLibrarySearch()

        fun onClickBtnBookSearch()

        fun onClickBtnMyLibrary()

        fun onClickBtnMyBook()
    }

    private var listener: ItemClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickListener) listener = context
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // 図書館検索ボタンをクリックしたときの処理
        binding.btnLibrarySearch.setOnClickListener {
            listener!!.onClickBtnLibrarySearch()
        }
        // 蔵書検索ボタンをクリックしたときの処理
        binding.btnBookSearch.setOnClickListener {
            listener!!.onClickBtnBookSearch()
        }
        // マイ図書館ボタンをクリックしたときの処理
        binding.btnMyLibrary.setOnClickListener {
            listener!!.onClickBtnMyLibrary()
        }
        // マイ本棚ボタンをクリックしたときの処理
        binding.btnMyBook.setOnClickListener {
            listener!!.onClickBtnMyBook()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
