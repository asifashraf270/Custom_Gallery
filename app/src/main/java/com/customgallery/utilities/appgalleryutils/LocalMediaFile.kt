package com.customgallery.utilities.appgalleryutils

import android.os.Parcel
import android.os.Parcelable

class LocalMediaFile : Parcelable {
    var bucketId = 0
    var mediaPath: String? = null
    var id = 0
    var mediaType: String? = null
    var videoThumbNail: String? = null


    var isSelected = false


    var fileUri: String? = null
    var videoDuration: String? = null
    var date: String? = null
    var selectedCount = 0

    constructor(
        bucketId: Int,
        mediaPath: String?,
        mediaType: String?,
        videoThumbNail: String?,
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
    constructor(`in`: Parcel) {
        id = `in`.readInt()
        bucketId = `in`.readInt()
        mediaPath = `in`.readString()
        mediaType = `in`.readString()
        videoThumbNail = `in`.readString()
        fileUri = `in`.readString()
        videoDuration = `in`.readString()
        date = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeInt(bucketId)
        dest.writeString(mediaPath)
        dest.writeString(mediaType)
        dest.writeString(videoThumbNail)
        dest.writeString(fileUri)
        dest.writeString(videoDuration)
        dest.writeString(date)
    }

    companion object CREATOR : Parcelable.Creator<LocalMediaFile> {
        var VIDEO_TYPE = "3"
        var IMAGE_TYPE = "1"
        override fun createFromParcel(parcel: Parcel): LocalMediaFile {
            return LocalMediaFile(parcel)
        }

        override fun newArray(size: Int): Array<LocalMediaFile?> {
            return arrayOfNulls(size)
        }
    }


}