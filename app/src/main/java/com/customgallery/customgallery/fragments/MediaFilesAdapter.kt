package com.customgallery.customgallery.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.customgallery.R
import com.customgallery.databinding.MediaFileItemViewBinding
import com.customgallery.utilities.appgalleryutils.LocalMediaFile

class MediaFilesAdapter :RecyclerView.Adapter<MediaFilesAdapter.MediaFilesViewHolder>(){
    var mediaFiles= mutableListOf<LocalMediaFile>()
    inner class MediaFilesViewHolder(val binding:MediaFileItemViewBinding):RecyclerView.ViewHolder(binding.root)

    fun updateMediaList(updatedList:List<LocalMediaFile>)
    {
        this.mediaFiles.clear()
        this.mediaFiles.addAll(updatedList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaFilesViewHolder {
        return MediaFilesViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.media_file_item_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MediaFilesViewHolder, position: Int) {
        holder.binding.mediaFile=mediaFiles.get(position)
    }

    override fun getItemCount(): Int {
        return mediaFiles.size
    }
}