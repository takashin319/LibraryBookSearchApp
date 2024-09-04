package com.example.librarybooksearchapp.view.mylibrary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.librarybooksearchapp.databinding.FragmentMyLibraryDataBinding
import com.example.librarybooksearchapp.viewmodel.MyLibraryViewModel
import kotlinx.coroutines.launch

class MyLibraryDataFragment : Fragment() {
    private var _binding: FragmentMyLibraryDataBinding? = null
    private val binding get() = _binding!!
    private val _myLibraryViewModel: MyLibraryViewModel by activityViewModels()

    // FragmentのイベントをActivityで受け取るためのリスナー
    interface ItemClickListener {
        fun onClickBtnBack()
    }

    private var listener: ItemClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickListener) listener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyLibraryDataBinding.inflate(inflater, container, false)
        binding.vm = _myLibraryViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // カーリルで開くをクリックしたときの処理
        binding.txtCalilLink.setOnClickListener {
            val uri = _myLibraryViewModel.createCalilLink()
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        // マイ図書館から削除するボタンをクリックしたときの処理
//        binding.btnDelete.setOnClickListener {
//            lifecycleScope.launch {
//                val job = _myLibraryViewModel.deleteMyLibrary(_myLibraryViewModel.selectLibrary)
//                job.join()
//            }
//            findNavController().popBackStack()
//        }

        binding.btnDelete.setOnClickListener {
            lifecycleScope.launch {
                _myLibraryViewModel.deleteMyLibrary(_myLibraryViewModel.selectLibrary)
            }
            findNavController().popBackStack()
        }

        // 閉じるボタンをクリックしたときの処理
        binding.btnClose.setOnClickListener {
            listener!!.onClickBtnBack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
