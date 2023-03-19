package com.example.selectvideothumbnail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.selectvideothumbnail.databinding.ItemPhotoBinding

class CustomAlbumAdapter(private var itemList: ArrayList<ItemGallery>) :
    RecyclerView.Adapter<CustomAlbumAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemGallery) {
            binding.tag = item

            binding.layoutCL.setOnClickListener {
                item.isSelected = !binding.selectRatioBT.isChecked
                binding.selectRatioBT.isChecked = item.isSelected
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = ItemPhotoBinding.inflate(inflater, parent, false)
        return MyViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setData(itemList: ArrayList<ItemGallery>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }
}