package com.example.librarybooksearchapp.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.librarybooksearchapp.databinding.LibraryListItemBinding
import com.example.librarybooksearchapp.model.database.DataLibraryEntity

class LibraryListAdapter :
    ListAdapter<DataLibraryEntity, LibraryListViewHolder>(
        LibraryListDiffCallback,
    ) {
    // リスナ変数を定義
    private lateinit var listener: OnLibraryItemClickListener

    // クリックイベントリスナ用のインターフェース
    interface OnLibraryItemClickListener {
        fun onItemClick(data: DataLibraryEntity)
    }

    // クリックイベントリスナ
    fun setOnItemClickListener(listener: OnLibraryItemClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(
        holder: LibraryListViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): LibraryListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LibraryListItemBinding.inflate(inflater, parent, false)
        return LibraryListViewHolder(binding)
    }
}

class LibraryListViewHolder(
    private val binding: LibraryListItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DataLibraryEntity) {
        binding.txtLibraryName.text = item.formal
        binding.txtAddress.text = item.address
    }
}

private object LibraryListDiffCallback : DiffUtil.ItemCallback<DataLibraryEntity>() {
    override fun areContentsTheSame(
        oldItem: DataLibraryEntity,
        newItem: DataLibraryEntity,
    ): Boolean = oldItem == newItem

    override fun areItemsTheSame(
        oldItem: DataLibraryEntity,
        newItem: DataLibraryEntity,
    ): Boolean = oldItem.libid == newItem.libid
}
