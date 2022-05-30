package com.customgallery.customgallery

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.customgallery.utilities.AppLogger
import com.customgallery.utilities.Resource
import com.customgallery.utilities.appgalleryutils.Bucket
import com.customgallery.utilities.appgalleryutils.LocalMediaFile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "MediaRepository"

@Singleton
class MediaRepository @Inject constructor(
    val contentResolver: ContentResolver, val context: Application
) {
    fun getImageBuckets(): List<Bucket>? {
        var imagesBucketList = mutableListOf<Bucket>()
        val images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media._ID, MediaStore.Video.Media.DATA


        )
        val BUCKET_ORDER_BY = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC"
        val cursor = context.contentResolver.query(
            images,
            projection,  // Which columns to return
            /* BUCKET_GROUP_BY*/
            null,  // Which rows to return (all rows)
            null,  // Selection arguments (none)
            BUCKET_ORDER_BY // Ordering
        )

        if (cursor == null) {
            return imagesBucketList
//            return imagesBuckets.value= Resource.success()
        }
        val addedBucketIds: MutableList<String> = ArrayList()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val bucketIdColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)
                val bucketColumnIndex =
                    cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val b = Bucket()
                b.id = cursor.getInt(bucketIdColumnIndex)
                b.name = cursor.getString(bucketColumnIndex)
                b.image_id = cursor.getInt(idColumnIndex)
                b.isCameraBucket = false
                b.filesCount = getCount(context, images, b.id.toString())
                b.fileType = 1
                val data = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                b.imageThumbnail = cursor.getString(data)
                if (b.name != null && !addedBucketIds.contains("" + b.id)) {
                    imagesBucketList.add(b)
                    addedBucketIds.add("" + b.id)
                }
            }

            cursor.close()
        }
        return imagesBucketList
    }

    @SuppressLint("Range")
    fun getVideoBuckets(): List<Bucket>? {
        var videoBucketList = mutableListOf<Bucket>()
        val images = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Thumbnails.DATA,
            MediaStore.Video.Media._ID
        )
        val BUCKET_ORDER_BY = MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " ASC"
        val BUCKET_GROUP_BY = "1) GROUP BY 1,(2"
        val cursor = context.contentResolver.query(
            images,
            projection,  // Which columns to return
            /*BUCKET_GROUP_BY*/
            null,  // Which rows to return (all rows)
            null,  // Selection arguments (none)
            BUCKET_ORDER_BY // Ordering
        )
//        val buckets: MutableList<Bucket> = ArrayList()
        if (cursor == null) {
            return videoBucketList
        }
        val addedBucketIds: MutableList<String> = ArrayList()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val bucketIdColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID)
                val bucketColumnIndex =
                    cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
                val b = Bucket()
                b.id = cursor.getInt(bucketIdColumnIndex)
                b.name = cursor.getString(bucketColumnIndex)
                b.filesCount = getCount(context, images, b.id.toString())
                b.fileType = LocalMediaFile.VIDEO_TYPE
                val thumbnail = cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)
                val idColumnIndex = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                var thumbnailBitmap = MediaStore.Video.Thumbnails.getThumbnail(
                    contentResolver,
                    idColumnIndex.toLong(),
                    MediaStore.Video.Thumbnails.MINI_KIND,
                    null
                )
                b.videoThumbnail = thumbnailBitmap
                b.imageThumbnail = cursor.getString(thumbnail)
                if (b.name != null && !addedBucketIds.contains("" + b.id)) {
                    videoBucketList.add(b)
                    addedBucketIds.add("" + b.id)
                }
            }
            cursor.close()

        }
        return videoBucketList
    }

    fun getAllImagesCount(contentUri: Uri): Int {
        context.contentResolver.query(contentUri, null, null, null, null)
            .use { cursor -> return if (cursor == null || cursor.moveToFirst() === false) 0 else cursor.count }
    }

    fun getCount(context: Context, contentUri: Uri, bucketId: String): Int {
        context.contentResolver.query(
            contentUri,
            null, MediaStore.Video.Media.BUCKET_ID + "=?", arrayOf(bucketId), null
        )
            .use { cursor -> return if (cursor == null || cursor.moveToFirst() === false) 0 else cursor.count }
    }

    @SuppressLint("Range")
    fun getAllVideoFiles(): List<LocalMediaFile>? {
        val images = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media._ID,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.Video.VideoColumns.DATE_ADDED,
            MediaStore.Video.Thumbnails.DATA
        )
        val selection = MediaStore.Video.Media.DURATION + " > ?"
        val arg = arrayOf("5000")
        val BUCKET_ORDER_BY = MediaStore.Video.Media._ID + " DESC"
        val cursor = context.contentResolver.query(
            images,
            projection,  // Which columns to return
            selection,  // Which rows to return (all rows)
            arg,  // Selection arguments (none)
            BUCKET_ORDER_BY // Ordering0
        )
        val buckets: MutableList<LocalMediaFile> = ArrayList()
        if (cursor == null) {
            return buckets
        }
        while (cursor.moveToNext()) {
            val bucketIdColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID)
            val bucketColumnIndex =
                cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val dataColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
            val thumbnail = cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)
//            val thumbnailPath = cursor.getString(thumbnail)
            val idColumnIndex = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID))
            val videoDuration =
                cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION))
            val date =
                cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_ADDED))
            var thumbnailBitmap = MediaStore.Video.Thumbnails.getThumbnail(
                contentResolver,
                idColumnIndex.toLong(),
                MediaStore.Video.Thumbnails.MINI_KIND,
                null
            )
            val localMediaFile = LocalMediaFile(
                cursor.getInt(bucketIdColumnIndex),
                cursor.getString(dataColumnIndex),
                LocalMediaFile.VIDEO_TYPE,
                thumbnailBitmap,
                idColumnIndex,
                ""
            )
            localMediaFile.videoDuration = videoDuration;
            localMediaFile.date = date

            buckets.add(localMediaFile)
        }
        cursor.close()
        return buckets
    }

    @SuppressLint("Range")
    fun getAllImagesFile(): List<LocalMediaFile>? {
        val images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
            MediaStore.Images.Media._ID,
            MediaStore.Images.ImageColumns.DATE_ADDED
        )
        val BUCKET_ORDER_BY = MediaStore.Images.Media._ID + " DESC"
        val cursor = context.contentResolver.query(
            images,
            projection,  // Which columns to return
            null,  // Which rows to return (all rows)
            null,  // Selection arguments (none)
            BUCKET_ORDER_BY // Ordering
        )
        val buckets: MutableList<LocalMediaFile> = ArrayList<LocalMediaFile>()
        if (cursor == null) {
            return buckets
        }
        while (cursor?.moveToNext()) {
            val bucketIdColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)
            val bucketColumnIndex =
                cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val idColumnIndex = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID))
            val date =
                cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED))
            val b = Bucket()
            // Log.e(TAG, idColumnIndex + "...file id");
            val thumbnail = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA)
            val thumbnailPath = cursor.getString(thumbnail)
            val localMediaFile = LocalMediaFile(
                cursor.getInt(bucketIdColumnIndex),
                cursor.getString(dataColumnIndex),
                LocalMediaFile.IMAGE_TYPE,
                null,
                idColumnIndex,
                "" /*Uri.fromFile(new File(thumbnailPath)).toString()*/
            )
            localMediaFile.date = date;

            buckets.add(localMediaFile)
        }
        cursor.close()
        return buckets
    }


    fun getFilesWithId(id: String, types: Int): MutableLiveData<Resource<List<LocalMediaFile>>> {
        var filesList = MediatorLiveData<Resource<List<LocalMediaFile>>>()
        filesList.value = Resource.loading()
        var allImages = getAllImagesFile()
        var allVideos = getAllVideoFiles()
        when (id.toInt()) {
            -1 -> {
                filesList.value = Resource.success(allImages, "Success")
            }
            -2 -> {
                filesList.value = Resource.success(allVideos, "Success")
            }
            else -> {
                when (types) {
                    1 -> {
                        filesList.value =
                            Resource.success(allImages?.let { getFiles(id.toInt(), it) }, "Success")
                    }
                    2 -> {
                        filesList.value =
                            Resource.success(allVideos?.let { getFiles(id.toInt(), it) }, "success")
                    }

                }
            }

        }

        return filesList

    }

    fun getFiles(id: Int, filesList: List<LocalMediaFile>): List<LocalMediaFile> {
        var list = mutableListOf<LocalMediaFile>()
        for (localMedia in filesList) {
            if (localMedia.bucketId == id) {
                list.add(localMedia)
            }
        }
        return list
    }


    fun getAllBuckets(): MediatorLiveData<Resource<List<Bucket>>> {
        var bucketsList = MediatorLiveData<Resource<List<Bucket>>>()
        bucketsList.value=Resource.loading()
        var buckets: MutableList<Bucket> = mutableListOf()
        var imagesBuckets = getImageBuckets()
        var videoBuckets = getVideoBuckets()
        var allImages = Bucket()
        allImages.id = -1
        allImages.name = "All images"
        allImages.fileType = 1
        allImages.filesCount = getAllImagesCount(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        AppLogger.errorMessage(TAG, allImages.filesCount.toString())
        allImages.imageThumbnail = imagesBuckets?.get(0)?.imageThumbnail
        buckets.add(allImages)
        var allVideos = Bucket()
        allVideos.id = -2;
        allVideos.name = "All Videos"
        allVideos.fileType = 2
        allVideos.filesCount = getAllImagesCount(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        allVideos.imageThumbnail = videoBuckets?.get(0)?.imageThumbnail
        allVideos.videoThumbnail = videoBuckets?.get(0)?.videoThumbnail
        buckets.add(allVideos)
        imagesBuckets?.let { buckets.addAll(it) }
        videoBuckets?.let { buckets.addAll(it) }

        bucketsList.value = Resource.success(buckets, "Success")
        return bucketsList

    }


}