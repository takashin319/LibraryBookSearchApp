package com.example.librarybooksearchapp.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.librarybooksearchapp.databinding.AreaSelectListItemBinding

class AreaSelectAdapter(
    private val itemList: List<String>,
) : RecyclerView.Adapter<AreaSelectAdapter.AreaSelectViewHolder>() {
    class AreaSelectViewHolder(
        val binding: AreaSelectListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    // リスナ変数を定義
    private lateinit var listener: OnItemClickListener

    // クリックイベントリスナ用のインターフェース
    interface OnItemClickListener {
        fun onItemClick(data: String)
    }

    // クリックイベントリスナ
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AreaSelectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AreaSelectListItemBinding.inflate(inflater, parent, false)
        return AreaSelectViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AreaSelectViewHolder,
        position: Int,
    ) {
        holder.binding.txtAreaName.text = itemList[position]

        // リストアイテムのクリックイベントリスナ
        holder.itemView.setOnClickListener {
            listener.onItemClick(itemList[position])
        }
    }

    override fun getItemCount() = itemList.size
}
