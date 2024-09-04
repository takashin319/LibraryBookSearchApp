package com.example.librarybooksearchapp.view.booksearch

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
import coil.load
import com.example.librarybooksearchapp.R
import com.example.librarybooksearchapp.databinding.FragmentBookDataBinding
import com.example.librarybooksearchapp.model.adapter.RentalStatusAdapter
import com.example.librarybooksearchapp.viewmodel.BookSearchViewModel
import kotlinx.coroutines.launch

class BookDataFragment : Fragment() {
    private var _binding: FragmentBookDataBinding? = null
    private val binding get() = _binding!!
    private val _bookSearchViewModel: BookSearchViewModel by activityViewModels()

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
        _binding = FragmentBookDataBinding.inflate(inflater, container, false)
        binding.vm = _bookSearchViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // サムネイル画像の読み込み
        binding.imgThumbnail.load(_bookSearchViewModel.selectBook.thumbnail) {
            error(R.drawable.baseline_image_not_supported_24)
        }

        // カーリルで開くをクリックしたときの処理
        binding.txtCalilLink.setOnClickListener {
            val uri = _bookSearchViewModel.createCalilLink()
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        // マイ本棚に追加ボタンをクリックしたときの処理
//        binding.btnInsertMyBook.setOnClickListener {
//            lifecycleScope.launch {
//                val job =
//                    _bookSearchViewModel.insertMyBook(_bookSearchViewModel.selectBook)
//                job.join()
//            }
//            Toast.makeText(context, "マイ本棚に追加しました。", Toast.LENGTH_SHORT).show()
//        }

        binding.btnInsertMyBook.setOnClickListener {
            lifecycleScope.launch {
                _bookSearchViewModel.insertMyBook(_bookSearchViewModel.selectBook)
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    _bookSearchViewModel.eventInsertMyBook.collect {
                        Toast.makeText(context, "マイ本棚に追加しました。", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 蔵書のカルーセル表示
//        lifecycleScope.launch {
//            val job = _bookSearchViewModel.getRentalStatus()
//            job.join()
//            if (_bookSearchViewModel.rentalStatusList.size != 0) {
//                binding.pagerRentalStatus.adapter =
//                    RentalStatusAdapter(_bookSearchViewModel.rentalStatusList)
//                val margin = view.context.resources.getDimension(R.dimen.view_pager_margin)
//                val offset = view.context.resources.getDimension(R.dimen.view_pager_offset)
//                binding.pagerRentalStatus.offscreenPageLimit = 2
//                binding.pagerRentalStatus.setPageTransformer { page, position ->
//                    val offset = position * (2 * offset + margin)
//                    page.translationX = -offset
//                }
//            } else {
//                binding.txtNoLibrary.visibility = View.VISIBLE
//            }
//            binding.progressBar.visibility = View.GONE
//        }

        lifecycleScope.launch {
            _bookSearchViewModel.getRentalStatus()
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                _bookSearchViewModel.eventGetRentalStatus.collect {
                    if (it.isNotEmpty()) {
                        binding.pagerRentalStatus.adapter =
                            RentalStatusAdapter(it)
                        val margin = view.context.resources.getDimension(R.dimen.view_pager_margin)
                        val offset = view.context.resources.getDimension(R.dimen.view_pager_offset)
                        binding.pagerRentalStatus.offscreenPageLimit = 2
                        binding.pagerRentalStatus.setPageTransformer { page, position ->
                            val offset = position * (2 * offset + margin)
                            page.translationX = -offset
                        }
                    } else {
                        binding.txtNoLibrary.visibility = View.VISIBLE
                    }
                    binding.progressBar.visibility = View.GONE
                }
            }
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
