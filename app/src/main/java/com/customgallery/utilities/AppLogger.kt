package com.customgallery.utilities

import android.util.Log
import com.customgallery.BuildConfig

class AppLogger {
    companion object {
        fun errorMessage(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                if (message.length > 4000) {
                    Log.e(tag, message.substring(0, 4000))
                    Log.e(tag, message.substring(4000, message.length))
                } else
                    Log.e(tag, message)
            }
        }

        fun verboseMessage(tag: String, message: String?) {
            if (BuildConfig.DEBUG) {
                Log.v(tag, message.toString())
            }
        }

        fun debugMessage(tag: String, message: String?) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, message.toString())
            }
        }

    }
}