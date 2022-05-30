package com.customgallery.customgallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.customgallery.utilities.AbsentLiveData
import com.customgallery.utilities.Resource
import com.customgallery.utilities.appgalleryutils.Bucket
import com.customgallery.utilities.appgalleryutils.LocalMediaFile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MediaViewModel @Inject constructor(
    repository: MediaRepository
) : ViewModel() {
    var loadAllBuckets = MutableLiveData<String>(null)
    var allBuckets: LiveData<Resource<List<Bucket>>> = Transformations.switchMap(loadAllBuckets)
    {
        if (it == null) {
            return@switchMap AbsentLiveData.create<Resource<List<Bucket>>>()
        } else {
            return@switchMap repository.getAllBuckets()
        }
    }

    //Get Files with Id
    var bucketId = MutableLiveData<String>()
    var fileType=MutableLiveData<Int>()

    var files: LiveData<Resource<List<LocalMediaFile>>> = Transformations.switchMap(bucketId)
    {
        if (it == null) {
            return@switchMap AbsentLiveData.create<Resource<List<LocalMediaFile>>>()
        } else {
            return@switchMap bucketId.value?.let { it1 -> repository.getFilesWithId(it1, fileType.value!!) }
        }
    }


}