package com.example.selectvideothumbnail

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.selectvideothumbnail.CustomAlbumAdapter
import com.example.selectvideothumbnail.ItemGallery
import com.example.selectvideothumbnail.R
import com.example.selectvideothumbnail.databinding.ActivityCustomalbumBinding
import kotlinx.coroutines.*
import java.util.*


class CustomAlbumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomalbumBinding
    private val listOfPhotos = ArrayList<ItemGallery>()
    private lateinit var customAlbumAdapter: CustomAlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customalbum)
        initView()
        initData()

    }

    private fun initView() {
        customAlbumAdapter = CustomAlbumAdapter(listOfPhotos)
        binding.photoRV.adapter = customAlbumAdapter
        val spanCount = 3
        val spacing = 20
        val includeEdge = false
        binding.photoRV.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
    }

    @SuppressLint("Range")
    private fun initData() {

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DURATION
        )
        var selection: String?
        val queryUri = MediaStore.Files.getContentUri("external")

        selection =
            (MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                    + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

        //이런 방법도 있다~
//            cursorLoader = CursorLoader(
//                this@CustomAlbumActivity,
//                queryUri,
//                projection,
//                selection,
//                null,  // Selection args (none).
//                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
//            )
//            cursor = cursorLoader.loadInBackground()

        var cursor: Cursor? = this@CustomAlbumActivity.contentResolver.query(
            queryUri,
            projection,
            selection,
            null,
            MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val data = ItemGallery()
                data.id =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
                data.mediaType =
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE))
                data.duration =
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.DURATION))

                if (data.mediaType == 1) {
                    data.mediaData = "content://media/external/images/media/" + data.id
                } else if (data.mediaType == 3) {
                    data.mediaData = "content://media/external/video/media/${data.id}"
                }
                listOfPhotos.add(data)
            } while (cursor.moveToNext())

        }

        cursor!!.close()
        customAlbumAdapter.setData(listOfPhotos)

        customAlbumAdapter.setOnItemClickListener(object : CustomAlbumAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: ItemGallery) {
                val intent = Intent(this@CustomAlbumActivity, MainActivity::class.java).apply {
                    putExtra("videoData",data.mediaData)
                }
                setResult(RESULT_OK,intent)
                finish()
            }

        })
    }


    inner class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount
            if (includeEdge) {
                outRect.left =
                    spacing - column * spacing / spanCount
                outRect.right =
                    (column + 1) * spacing / spanCount
                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right =
                    spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }
}