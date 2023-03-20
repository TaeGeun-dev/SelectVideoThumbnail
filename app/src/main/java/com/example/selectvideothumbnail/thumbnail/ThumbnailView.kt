package com.example.selectvideothumbnail.thumbnail

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.selectvideothumbnail.R

class ThumbnailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    init {
        scaleType = ScaleType.CENTER_CROP
        setColorFilter(ContextCompat.getColor(context, R.color.video_dimmed))
        val dimension = resources.getDimensionPixelSize(R.dimen.frames_video_width)
        val dimension1 = resources.getDimensionPixelSize(R.dimen.frames_video_height)
        layoutParams = LinearLayout.LayoutParams(dimension, dimension1).apply { weight = 1f }
    }
}