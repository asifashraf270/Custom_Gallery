package com.customgallery.customgallery.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.customgallery.R
import com.customgallery.databinding.AppGalleryItemViewBinding
import com.customgallery.utilities.AppLogger
import com.customgallery.utilities.appgalleryutils.Bucket

private const val TAG = "AppGalleryAdapter"

class AppGalleryAdapter : RecyclerView.Adapter<AppGalleryAdapter.AppGalleryViewHolder>() {
    var bucketsList = mutableListOf<Bucket>()
    lateinit var onclickListener: View.OnClickListener

    fun updateData(bucketsList: List<Bucket>) {
        this.bucketsList.clear()
        this.bucketsList.addAll(bucketsList)
        notifyDataSetChanged()
    }

    inner class AppGalleryViewHolder(val binding: AppGalleryItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppGalleryViewHolder {
        AppLogger.errorMessage(TAG, "onCreateView")
        return AppGalleryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.app_gallery_item_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AppGalleryViewHolder, position: Int) {
        var bucket = bucketsList.get(position)
        holder.binding.bucketdata = bucket
        AppLogger.errorMessage(TAG, bucket.id.toString())
        holder.binding.rootView.setTag(position)
        holder.binding.rootView.setOnClickListener(onclickListener)

    }

    override fun getItemCount(): Int {
        return bucketsList.size
    }
}