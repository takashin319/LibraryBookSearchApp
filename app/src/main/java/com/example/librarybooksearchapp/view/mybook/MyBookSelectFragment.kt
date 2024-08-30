package com.example.librarybooksearchapp.view.mybook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librarybooksearchapp.R
import com.example.librarybooksearchapp.databinding.FragmentMyBookSelectBinding
import com.example.librarybooksearchapp.model.adapter.BookListAdapter
import com.example.librarybooksearchapp.model.database.DataBookEntity
import com.example.librarybooksearchapp.viewmodel.MyBookViewModel

class MyBookSelectFragment : Fragment() {
    private var _binding: FragmentMyBookSelectBinding? = null
    private val binding get() = _binding!!
    private val _myBookViewModel: MyBookViewModel by activityViewModels()
    private val adapter = BookListAdapter()

    // LiveDataのオブザーバクラス
    private inner class MyBookListObserver : Observer<List<DataBookEntity>> {
        override fun onChanged(value: List<DataBookEntity>) {
            adapter.submitList(value)
            binding.txtNoBook.isVisible = value.isEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyBookSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // LiveDataにオブザーバを登録
        _myBookViewModel.myBookList.observe(
            viewLifecycleOwner,
            MyBookListObserver(),
        )

        // RecyclerViewの設定
        binding.rvMyBookList.adapter = adapter
        binding.rvMyBookList.layoutManager =
            LinearLayoutManager(this.context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        binding.rvMyBookList.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL,
            ),
        )

        // リストをタップしたときの処理
        adapter.setOnItemClickListener(
            object : BookListAdapter.OnBookItemClickListener {
                override fun onItemClick(data: DataBookEntity) {
                    _myBookViewModel.selectBook = data
                    findNavController().navigate(R.id.action_myBookSelectFragment_to_myBookDataFragment)
                }
            },
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
