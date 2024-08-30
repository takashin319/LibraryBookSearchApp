package com.example.librarybooksearchapp.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.librarybooksearchapp.R
import com.example.librarybooksearchapp.databinding.BookListItemBinding
import com.example.librarybooksearchapp.model.database.DataBookEntity

class BookListAdapter : ListAdapter<DataBookEntity, BookListViewHolder>(BookListDiffCallback) {
    // リスナ変数を定義
    private lateinit var listener: OnBookItemClickListener

    // クリックイベントリスナ用のインターフェース
    interface OnBookItemClickListener {
        fun onItemClick(data: DataBookEntity)
    }

    // クリックイベントリスナ
    fun setOnItemClickListener(listener: OnBookItemClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(
        holder: BookListViewHolder,
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
    ): BookListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BookListItemBinding.inflate(inflater, parent, false)
        return BookListViewHolder(binding)
    }
}

class BookListViewHolder(
    private val binding: BookListItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: DataBookEntity) {
        binding.txtTitle.text = item.title
        binding.txtAuthors.text = item.authors
        binding.txtPublishedDate.text = item.publishedDate

        binding.imgThumbnail.load(item.thumbnail) {
            error(R.drawable.baseline_image_not_supported_24)
        }
    }
}

private object BookListDiffCallback : DiffUtil.ItemCallback<DataBookEntity>() {
    override fun areContentsTheSame(
        oldItem: DataBookEntity,
        newItem: DataBookEntity,
    ): Boolean = oldItem == newItem

    override fun areItemsTheSame(
        oldItem: DataBookEntity,
        newItem: DataBookEntity,
    ): Boolean = oldItem.isbn == newItem.isbn
}
