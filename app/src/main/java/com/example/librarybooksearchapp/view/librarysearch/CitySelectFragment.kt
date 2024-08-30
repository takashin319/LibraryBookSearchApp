package com.example.librarybooksearchapp.view.librarysearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librarybooksearchapp.R
import com.example.librarybooksearchapp.databinding.FragmentCitySelectBinding
import com.example.librarybooksearchapp.model.adapter.AreaSelectAdapter
import com.example.librarybooksearchapp.viewmodel.LibrarySearchViewModel

class CitySelectFragment : Fragment() {
    private var _binding: FragmentCitySelectBinding? = null
    private val binding get() = _binding!!
    private val _librarySearchViewModel: LibrarySearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCitySelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerViewの設定
        val adapter =
            AreaSelectAdapter(_librarySearchViewModel.cityList(_librarySearchViewModel.selectPref))
        binding.rvCitySelect.setHasFixedSize(true)
        binding.rvCitySelect.adapter = adapter
        binding.rvCitySelect.layoutManager =
            LinearLayoutManager(this.context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        binding.rvCitySelect.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL,
            ),
        )

        // リストをタップした時の処理
        adapter.setOnItemClickListener(
            object : AreaSelectAdapter.OnItemClickListener {
                override fun onItemClick(data: String) {
                    _librarySearchViewModel.selectCity = data
                    findNavController().navigate(R.id.action_citySelectFragment_to_libraryListFragment)
                }
            },
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
