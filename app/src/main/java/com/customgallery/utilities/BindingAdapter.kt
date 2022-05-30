package com.customgallery.utilities

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.customgallery.utilities.appgalleryutils.LocalMediaFile
import java.io.File

private const val TAG = "BindingAdapter"

@BindingAdapter(value = ["imageUrl", "fileType", "videothumbnail"], requireAll = true)
fun loadImage(imageview: ImageView, imageUrl: String, fileType: Int?, videothumbnail: Bitmap?) {
    AppLogger.errorMessage(TAG, imageUrl)
    if (fileType == LocalMediaFile.IMAGE_TYPE) {
        var image = File(imageUrl)
        if (image.exists()) {
            imageview.setImageURI(Uri.fromFile(image))
        }
    } else {
        imageview.setImageBitmap(videothumbnail)
    }
}

@BindingAdapter("videoFile")
fun isVideoFile(imageview: ImageView, fileType: Int?) {
    if (fileType == LocalMediaFile.IMAGE_TYPE) {
        imageview.visibility = View.GONE
    } else {
        imageview.visibility = View.VISIBLE
    }
}