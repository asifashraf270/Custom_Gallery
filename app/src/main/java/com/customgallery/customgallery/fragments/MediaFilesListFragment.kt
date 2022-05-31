package com.customgallery.customgallery.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.customgallery.R
import com.customgallery.customgallery.MediaViewModel
import com.customgallery.databinding.FragmentMediaFilesListBinding
import com.customgallery.utilities.AppLogger
import com.customgallery.utilities.Status
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MediaFilesListFragment"

@AndroidEntryPoint
class MediaFilesListFragment : Fragment() {
    private val viewModel: MediaViewModel by activityViewModels()
    lateinit var adapter: MediaFilesAdapter

    lateinit var binding: FragmentMediaFilesListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppLogger.errorMessage(TAG, "oncreateView")
        // Inflate the layout for this fragment
        binding = FragmentMediaFilesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setUpEventObserver()
    }

    fun setUpAdapter() {
        adapter = MediaFilesAdapter()
        binding.mediaFile.layoutManager = GridLayoutManager(context, 4)
        binding.mediaFile.adapter = adapter;
    }

    fun setUpEventObserver() {
        viewModel.files.observe(viewLifecycleOwner) {
            AppLogger.errorMessage(TAG, "files")
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> adapter.updateMediaList(it1) }
                    AppLogger.errorMessage(TAG, it.data?.size.toString())
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        AppLogger.errorMessage(TAG, "onPause")
    }


}