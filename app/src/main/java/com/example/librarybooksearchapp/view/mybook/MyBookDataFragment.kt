package com.example.librarybooksearchapp.view.mybook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.librarybooksearchapp.R
import com.example.librarybooksearchapp.databinding.FragmentMyBookDataBinding
import com.example.librarybooksearchapp.model.adapter.RentalStatusAdapter
import com.example.librarybooksearchapp.viewmodel.MyBookViewModel
import kotlinx.coroutines.launch

class MyBookDataFragment : Fragment() {
    private var _binding: FragmentMyBookDataBinding? = null
    private val binding get() = _binding!!
    private val _myBookViewModel: MyBookViewModel by activityViewModels()

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
        _binding = FragmentMyBookDataBinding.inflate(inflater, container, false)
        binding.vm = _myBookViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // サムネイル画像の読み込み
        binding.imgThumbnail.load(_myBookViewModel.selectBook.thumbnail) {
            error(R.drawable.baseline_image_not_supported_24)
        }

        // カーリルで開くをクリックしたときの処理
        binding.txtCalilLink.setOnClickListener {
            val uri = _myBookViewModel.createCalilLink()
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        // マイ本棚から削除するボタンをクリックしたときの処理
//        binding.btnDeleteMyBook.setOnClickListener {
//            lifecycleScope.launch {
//                _myBookViewModel.deleteMyBook(_myBookViewModel.selectBook)
//            }
//            findNavController().popBackStack()
//        }

        binding.btnDeleteMyBook.setOnClickListener {
            _myBookViewModel.deleteMyBook(_myBookViewModel.selectBook)
            findNavController().popBackStack()
        }

        // 蔵書のカルーセル表示
//        lifecycleScope.launch {
//            _myBookViewModel.getRentalStatus()
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                _myBookViewModel.eventGetRentalStatus.collect {
//                    if (it.isNotEmpty()) {
//                        binding.pagerRentalStatus.adapter =
//                            RentalStatusAdapter(it)
//                        val margin = view.context.resources.getDimension(R.dimen.view_pager_margin)
//                        val offset = view.context.resources.getDimension(R.dimen.view_pager_offset)
//                        binding.pagerRentalStatus.offscreenPageLimit = 2
//                        binding.pagerRentalStatus.setPageTransformer { page, position ->
//                            val offset = position * (2 * offset + margin)
//                            page.translationX = -offset
//                        }
//                    } else {
//                        binding.txtNoLibrary.visibility = View.VISIBLE
//                    }
//                    binding.progressBar.visibility = View.GONE
//                }
//            }
//        }

        // 蔵書情報の取得
        _myBookViewModel.getRentalStatus()

        // 閉じるボタンをクリックしたときの処理
        binding.btnClose.setOnClickListener {
            listener!!.onClickBtnBack()
        }

        // ViewModelのFlowの購読
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    // 蔵書のカルーセル表示
                    _myBookViewModel.eventGetRentalStatus.collect {
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
