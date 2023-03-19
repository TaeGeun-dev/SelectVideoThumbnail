package com.example.selectvideothumbnail

import java.text.SimpleDateFormat
import java.util.*

class Utils {

    /**
     * @param duration
     * @return
     */
    companion object {
        fun getPrettyDuration(duration: Long): String? {
            var result = ""
            val sdf: SimpleDateFormat
            try {
                val hour = 60 * 60 * 1000
                sdf = if (hour <= duration) {
                    SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                } else {
                    SimpleDateFormat("mm:ss", Locale.getDefault())
                }
                result = sdf.format(Date(duration - TimeZone.getDefault().rawOffset))
            } catch (e: Exception) {
                // HouseLog.e("NHS", "e=" + e.message)
            }
            return result
        }
    }
}