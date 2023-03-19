package com.example.selectvideothumbnail

import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.selectvideothumbnail.Utils


class ItemGallery {

    var mediaData: String = ""
    var id: Long = -1
    var isSelected = false
    var mediaType: Int// 사진: 1, 동영상: 3
    var duration: Int// 동영상 재생시간


    companion object {

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String) {
            Glide.with(view.context).load(url).into(view)
        }

        @JvmStatic
        @BindingAdapter("duration")
        fun setDuration(view: TextView, duration: Long) {
            if (duration == 0L) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
                view.text = Utils.getPrettyDuration(duration)
            }
        }

        @JvmStatic
        @BindingAdapter("radioButton")
        fun setRadioButton(view: RadioButton, isSelected: Boolean) {
            view.isChecked = isSelected
        }
    }

    constructor() {
        mediaData = ""
        id = -1
        isSelected = false
        mediaType = -1
        duration = 0
    }

}