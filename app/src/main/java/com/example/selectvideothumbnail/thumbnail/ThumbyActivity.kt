package com.example.selectvideothumbnail.thumbnail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import com.example.selectvideothumbnail.R
import com.example.selectvideothumbnail.databinding.ActivityThumbyBinding


class ThumbyActivity : ComponentActivity() {

    companion object {
        const val THUMBNAIL_POSITION = "thumbnail_position"
        const val VIDEO_URI = "video_url"
        const val VIDEO_THUMBNAIL_RESULT_OK = 1001
        fun getStartIntent(context: Context, uri: Uri, thumbnailPosition: Long = 0): Intent {
            val intent = Intent(context, ThumbyActivity::class.java)
            intent.putExtra(VIDEO_URI, uri)
            intent.putExtra(THUMBNAIL_POSITION, thumbnailPosition)
            return intent
        }
    }

    private lateinit var videoUri: Uri
    private var location: Int = 0
    private lateinit var binding: ActivityThumbyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thumby)
        binding.uploadCV.setOnClickListener { finishWithData() }
        binding.closeIV.setOnClickListener { finish() }

        videoUri = intent.getParcelableExtra(VIDEO_URI)!!

        setupVideoContent(videoUri)

    }

    private fun setupVideoContent(videoUri: Uri) {
        binding.thumbnailCCV.setDataSource(this, videoUri)

        binding.thumbsTTL.seekListener = seekListener
        binding.thumbsTTL.currentSeekPosition = intent.getLongExtra(THUMBNAIL_POSITION, 0).toFloat()
        binding.thumbsTTL.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.thumbsTTL.viewTreeObserver.removeOnGlobalLayoutListener(this)
                binding.thumbsTTL.uri = videoUri
            }
        })
    }

    private fun finishWithData() {
        val intent = Intent()
        intent.putExtra(
            THUMBNAIL_POSITION,
            location
        )
        intent.putExtra(VIDEO_URI, videoUri)
        setResult(VIDEO_THUMBNAIL_RESULT_OK, intent)
        finish()
    }

    private val seekListener = object : SeekListener {
        override fun onVideoSeeked(percentage: Double) {
            location = (percentage.toInt() * binding.thumbnailCCV.getDuration()) / 100
            binding.thumbnailCCV.seekTo((percentage.toInt() * binding.thumbnailCCV.getDuration()) / 100)
        }
    }
}