package com.example.selectvideothumbnail.thumbnail

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.example.selectvideothumbnail.R
import kotlinx.android.synthetic.main.view_timeline.view.*

class ThumbnailTimeline @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs) {

    private var frameDimensionW: Int = 0
    private var frameDimensionH: Int = 0
    var currentProgress = 0.0
    var currentSeekPosition = 0f
    var seekListener: SeekListener? = null
    var uri: Uri? = null
        set(value) {
            field = value
            field?.let {
                loadThumbnails(it)
                invalidate()
                view_seek_bar.setDataSource(context, it, 4)
                view_seek_bar.seekTo(currentSeekPosition.toInt())
            }
        }

    init {
        View.inflate(getContext(), R.layout.view_timeline, this)
        frameDimensionW = context.resources.getDimensionPixelOffset(R.dimen.frames_video_width)
        frameDimensionH = context.resources.getDimensionPixelOffset(R.dimen.frames_video_height)
        isFocusable = true
        isFocusableInTouchMode = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) elevation = 8f
        val margin = 50
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(margin, 0, margin, 0)
        layoutParams = params
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_MOVE -> handleTouchEvent(event)
        }
        return true
    }

    private fun handleTouchEvent(event: MotionEvent) {
        val seekViewWidth = context.resources.getDimensionPixelSize(R.dimen.frames_video_width)
        currentSeekPosition = (Math.round(event.x) - (seekViewWidth / 2)).toFloat()

        val availableWidth = container_thumbnails.width -
                (layoutParams as RelativeLayout.LayoutParams).marginEnd -
                (layoutParams as RelativeLayout.LayoutParams).marginStart
        if (currentSeekPosition + seekViewWidth > container_thumbnails.right) {
            currentSeekPosition = (container_thumbnails.right - seekViewWidth).toFloat()
        } else if (currentSeekPosition < container_thumbnails.left) {
            currentSeekPosition = paddingStart.toFloat()
        }

        currentProgress = (currentSeekPosition.toDouble() / availableWidth.toDouble()) * 100
        container_seek_bar.translationX = currentSeekPosition
        view_seek_bar.seekTo(((currentProgress * view_seek_bar.getDuration()) / 100).toInt())

        seekListener?.onVideoSeeked(currentProgress)
    }

    private fun loadThumbnails(uri: Uri) {
        val metaDataSource = MediaMetadataRetriever()
        metaDataSource.setDataSource(context, uri)

        val videoLength = (metaDataSource.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_DURATION
        )?.toInt()?.times(1000))?.toLong()

        val thumbnailCount = 10

        val interval = videoLength?.div(thumbnailCount)

        for (i in 0 until thumbnailCount) {
            val frameTime = i * interval!!
            var bitmap =
                metaDataSource.getFrameAtTime(frameTime, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            try {
                var targetWidth: Int = 0
                var targetHeight: Int = 0
                if (bitmap != null) {
                    if (bitmap.height > bitmap.width) {
                        targetHeight = frameDimensionH
                        val percentage = frameDimensionH.toFloat() / bitmap.height
                        targetWidth = (bitmap.width * percentage).toInt()
                    } else {
                        targetWidth = frameDimensionW
                        val percentage = frameDimensionW.toFloat() / bitmap.width
                        targetHeight = (bitmap.height * percentage).toInt()
                    }
                }
                bitmap =
                    bitmap?.let { Bitmap.createScaledBitmap(it, targetWidth, targetHeight, false) }
            } catch (e: Exception) {
                e.printStackTrace()
            }


            container_thumbnails.addView(ThumbnailView(context).apply { setImageBitmap(bitmap) })
        }
        metaDataSource.release()
    }
}