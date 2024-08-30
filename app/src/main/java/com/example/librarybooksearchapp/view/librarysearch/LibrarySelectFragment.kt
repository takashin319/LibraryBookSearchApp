package com.example.librarybooksearchapp.view.librarysearch

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
import com.example.librarybooksearchapp.databinding.FragmentLibrarySelectBinding
import com.example.librarybooksearchapp.model.adapter.LibraryListAdapter
import com.example.librarybooksearchapp.model.database.DataLibraryEntity
import com.example.librarybooksearchapp.viewmodel.LibrarySearchViewModel

class LibrarySelectFragment : Fragment() {
    private var _binding: FragmentLibrarySelectBinding? = null
    private val binding get() = _binding!!
    private val _librarySearchViewModel: LibrarySearchViewModel by activityViewModels()
    private val adapter = LibraryListAdapter()

    // LiveDataのオブザーバクラス
    private inner class LibraryListObserver : Observer<List<DataLibraryEntity>> {
        override fun onChanged(value: List<DataLibraryEntity>) {
            adapter.submitList(value)
            binding.txtNoLibrary.isVisible = value.isEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLibrarySelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // LiveDataにオブザーバを登録
        _librarySearchViewModel.libraryList.observe(
            viewLifecycleOwner,
            LibraryListObserver(),
        )

        _librarySearchViewModel.getLibraryData()

        // RecyclerViewの設定
        binding.rvLibraryList.adapter = adapter
        binding.rvLibraryList.layoutManager =
            LinearLayoutManager(this.context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        binding.rvLibraryList.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL,
            ),
        )

        // リストをタップしたときの処理
        adapter.setOnItemClickListener(
            object : LibraryListAdapter.OnLibraryItemClickListener {
                override fun onItemClick(data: DataLibraryEntity) {
                    _librarySearchViewModel.selectLibrary = data
                    findNavController().navigate(R.id.action_libraryListFragment_to_libraryDataFragment)
                }
            },
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
