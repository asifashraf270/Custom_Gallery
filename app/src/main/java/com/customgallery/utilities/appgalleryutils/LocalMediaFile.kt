package com.customgallery.utilities.appgalleryutils

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class LocalMediaFile : Serializable {
    var bucketId = 0
    var mediaPath: String? = null
    var id = 0
    var mediaType: Int? = null
    var videoThumbNail: Bitmap? = null


    var isSelected = false


    var fileUri: String? = null
    var videoDuration: String? = null
    var date: String? = null
    var selectedCount = 0

    constructor(
        bucketId: Int,
        mediaPath: String?,
        mediaType: Int?,
        videoThumbNail: Bitmap?,
        id: Int,
        fileUri: String?
    ) {
        this.bucketId = bucketId
        this.mediaPath = mediaPath
        this.mediaType = mediaType
        this.videoThumbNail = videoThumbNail
        this.id = id
        this.fileUri = fileUri
    }

    constructor() {}


    companion object {
        var VIDEO_TYPE = 2
        var IMAGE_TYPE = 1
    }


}