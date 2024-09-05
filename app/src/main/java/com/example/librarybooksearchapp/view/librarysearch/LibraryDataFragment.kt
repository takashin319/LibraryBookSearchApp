package com.example.librarybooksearchapp.view.librarysearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.librarybooksearchapp.databinding.FragmentLibraryDataBinding
import com.example.librarybooksearchapp.viewmodel.LibrarySearchViewModel
import kotlinx.coroutines.launch

class LibraryDataFragment : Fragment() {
    private var _binding: FragmentLibraryDataBinding? = null
    private val binding get() = _binding!!
    private val _librarySearchViewModel: LibrarySearchViewModel by activityViewModels()

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
        _binding = FragmentLibraryDataBinding.inflate(inflater, container, false)
        binding.vm = _librarySearchViewModel
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
            val uri = _librarySearchViewModel.createCalilLink()
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        // マイ図書館に追加ボタンをクリックしたときの処理
//        binding.btnInsertMyLibrary.setOnClickListener {
//            lifecycleScope.launch {
//                _librarySearchViewModel.insertMyLibrary(_librarySearchViewModel.selectLibrary)
//                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    _librarySearchViewModel.eventInsertMyLibrary.collect {
//                        Toast.makeText(context, "マイ図書館に追加しました。", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }

        binding.btnInsertMyLibrary.setOnClickListener {
            _librarySearchViewModel.insertMyLibrary(_librarySearchViewModel.selectLibrary)
        }

        // 閉じるボタンをクリックしたときの処理
        binding.btnClose.setOnClickListener {
            listener!!.onClickBtnBack()
        }

        // ViewModelのFlowの購読
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                _librarySearchViewModel.eventInsertMyLibrary.collect {
                    launch {
                        Toast
                            .makeText(context, "マイ図書館に追加しました。", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
