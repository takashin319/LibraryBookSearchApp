package com.example.librarybooksearchapp.view.booksearch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librarybooksearchapp.R
import com.example.librarybooksearchapp.databinding.FragmentBookSearchBinding
import com.example.librarybooksearchapp.model.adapter.BookListAdapter
import com.example.librarybooksearchapp.model.database.DataBookEntity
import com.example.librarybooksearchapp.viewmodel.BookSearchViewModel

class BookSearchFragment : Fragment() {
    private var _binding: FragmentBookSearchBinding? = null
    private val binding get() = _binding!!
    private val _bookSearchViewModel: BookSearchViewModel by activityViewModels()
    private val adapter = BookListAdapter()

    // LiveDataのオブザーバクラス
    private inner class BookListObserver : Observer<List<DataBookEntity>> {
        override fun onChanged(value: List<DataBookEntity>) {
            adapter.submitList(value)
        }
    }

    private inner class SearchStatusObserver : Observer<Boolean> {
        override fun onChanged(value: Boolean) {
            if (value) {
                binding.btnBookSearch.isEnabled = false
                binding.btnShowMore.isEnabled = false
                binding.txtSearchWord.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.btnBookSearch.isEnabled = true
                binding.btnShowMore.isEnabled = true
                binding.txtSearchWord.isEnabled = true
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBookSearchBinding.inflate(inflater, container, false)
        binding.vm = _bookSearchViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // LiveDataにオブザーバを登録
        _bookSearchViewModel.bookList.observe(
            viewLifecycleOwner,
            BookListObserver(),
        )

        _bookSearchViewModel.searchStatus.observe(
            viewLifecycleOwner,
            SearchStatusObserver(),
        )

        // RecyclerViewの設定
        binding.rvBookList.adapter = adapter
        binding.rvBookList.layoutManager =
            LinearLayoutManager(this.context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        binding.rvBookList.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL,
            ),
        )

        // リストをタップしたときの処理
        adapter.setOnItemClickListener(
            object : BookListAdapter.OnBookItemClickListener {
                override fun onItemClick(data: DataBookEntity) {
                    _bookSearchViewModel.selectBook = data
                    findNavController().navigate(R.id.action_bookSearchFragment_to_bookDataFragment)
                }
            },
        )

        // 検索ボタンをタップした時の処理
        binding.btnBookSearch.setOnClickListener {
            hideSoftKeyboard()
            if (binding.txtSearchWord.text.isNullOrBlank()) {
                Toast.makeText(context, "キーワードを入力してください", Toast.LENGTH_SHORT).show()
            } else {
                _bookSearchViewModel.q = binding.txtSearchWord.text.toString()
                _bookSearchViewModel.clearBookList()
                _bookSearchViewModel.addBookList(_bookSearchViewModel.q)
                binding.rvBookList.smoothScrollToPosition(0)
            }
        }

        // さらに表示ボタンをタップした時の処理
        binding.btnShowMore.setOnClickListener {
            if (_bookSearchViewModel.q.isNullOrBlank()) {
                Toast.makeText(context, "キーワードを入力してください", Toast.LENGTH_SHORT).show()
            } else {
                _bookSearchViewModel.addBookList(_bookSearchViewModel.q)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // キーボードを非表示にするメソッド
    private fun hideSoftKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}
