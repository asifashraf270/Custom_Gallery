package com.customgallery.utilities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionUtils {
    companion object
    {
        fun isExternalStoragePermission(context:Context):Boolean{
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                return false
            }
            return true
        }
    }
}