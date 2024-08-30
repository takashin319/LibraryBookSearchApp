package com.example.librarybooksearchapp.model.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.librarybooksearchapp.databinding.RentalStatusItemBinding
import com.example.librarybooksearchapp.model.database.DataRentalStatus

class RentalStatusAdapter(
    private val itemList: List<DataRentalStatus>,
) : RecyclerView.Adapter<RentalStatusAdapter.RentalStatusViewHolder>() {
    class RentalStatusViewHolder(
        val binding: RentalStatusItemBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RentalStatusViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RentalStatusItemBinding.inflate(inflater, parent, false)
        return RentalStatusViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RentalStatusViewHolder,
        position: Int,
    ) {
        holder.binding.txtLibraryName.text = itemList[position].formal
        holder.binding.txtRentalStatus.text = itemList[position].libkey
    }

    override fun getItemCount() = itemList.size
}
