package com.example.selectvideothumbnail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.selectvideothumbnail.databinding.ItemPhotoBinding

class CustomAlbumAdapter(private var itemList: ArrayList<ItemGallery>) :
    RecyclerView.Adapter<CustomAlbumAdapter.MyViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(v: View, data: ItemGallery)
    }
    private var listener: OnItemClickListener? =null
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
    inner class MyViewHolder(val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemGallery) {
            binding.tag = item

            binding.layoutCL.setOnClickListener {
                if (item.mediaType == 3) {
                    listener?.onItemClick(itemView, item)
                } else {
                    item.isSelected = !binding.selectRatioBT.isChecked
                    binding.selectRatioBT.isChecked = item.isSelected
                }
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