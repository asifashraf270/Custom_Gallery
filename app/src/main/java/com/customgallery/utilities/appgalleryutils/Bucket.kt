package com.customgallery.utilities.appgalleryutils

class Bucket {
    var id = 0  //id=-1 means all images id=-2 means all video
    var name: String? = null
    var image_id = -1
    var isCameraBucket = false
    var filesCount:Int?=0
    var imageThumbnail:String?=""
    var fileType:Int?=0 //fileType 1 mean image 2 mean videos
}