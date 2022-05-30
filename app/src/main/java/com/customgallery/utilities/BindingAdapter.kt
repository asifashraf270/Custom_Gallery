package com.customgallery.utilities

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import java.io.File

private const val TAG = "BindingAdapter"
@BindingAdapter("image")
fun loadImage(imageview: ImageView, url: String) {
    AppLogger.errorMessage(TAG,url)
    var image=File(url)
    if(image.exists())
    {
        imageview.setImageURI(Uri.fromFile(image))
    }
}