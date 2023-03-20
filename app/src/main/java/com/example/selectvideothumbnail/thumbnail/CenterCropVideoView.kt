package com.example.selectvideothumbnail.thumbnail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import com.yqritc.scalablevideoview.ScalableType
import com.yqritc.scalablevideoview.ScaleManager
import com.yqritc.scalablevideoview.Size


class CenterCropVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextureView(context, attrs), TextureView.SurfaceTextureListener {

    private var mediaPlayer: MediaPlayer? = null

    private var videoHeight = 0f
    private var videoWidth = 0f
    private var videoSizeDivisor = 1
    private var mScalableType = ScalableType.CENTER_CROP

    init {
        surfaceTextureListener = this
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = false

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        mediaPlayer?.setSurface(Surface(surfaceTexture))
    }

    fun setDataSource(context: Context, uri: Uri, videoSizeDivisor: Int = 1) {
        this.videoSizeDivisor = videoSizeDivisor
        initPlayer()
        mediaPlayer?.setDataSource(context, uri)
        prepare()
    }

    fun seekTo(milliseconds: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaPlayer?.seekTo(milliseconds.toLong(), MediaPlayer.SEEK_CLOSEST)
        }
    }

    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    private fun prepare() {
        mediaPlayer?.setOnVideoSizeChangedListener { _, width, height ->
            videoWidth = width.toFloat() / videoSizeDivisor
            videoHeight = height.toFloat() / videoSizeDivisor
            updateTextureViewSize()
            seekTo(0)
        }
        mediaPlayer?.prepareAsync()
    }

    private fun updateTextureViewSize() {
        val viewSize = Size(width, height)
        val videoSize = Size(videoWidth.toInt(), videoHeight.toInt())
        val scaleManager = ScaleManager(viewSize, videoSize)
        val matrix = scaleManager.getScaleMatrix(mScalableType)

        setTransform(matrix)

    }

     fun initPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }
    }
}