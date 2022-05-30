package com.customgallery.customgallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.customgallery.R
import com.customgallery.customgallery.MediaViewModel
import com.customgallery.databinding.FragmentAppGalleryBinding
import com.customgallery.utilities.AppLogger
import com.customgallery.utilities.Status
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AppGalleryFragment"

@AndroidEntryPoint
class AppGallery : Fragment(), View.OnClickListener {
    private val viewModel: MediaViewModel by activityViewModels()
    lateinit var adapter: AppGalleryAdapter
    lateinit var binding: FragmentAppGalleryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppLogger.errorMessage(TAG, "oncreateView")
        binding = FragmentAppGalleryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        eventObserverListener()
    }

    fun setUpAdapter() {
        adapter = AppGalleryAdapter()
        binding.appGallery.layoutManager = GridLayoutManager(context, 2)
        binding.appGallery.adapter = adapter
        adapter.onclickListener = this
    }

    fun eventObserverListener() {
        viewModel.loadAllBuckets.value = ""
        viewModel.allBuckets.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBarr.visibility = View.GONE
                    it.data?.let { it1 -> adapter.updateData(it1) }
                    AppLogger.errorMessage(TAG, it.data?.size.toString())
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {
                    binding.progressBarr.visibility = View.VISIBLE
                }
            }
        }
        viewModel.bucketId.observe(viewLifecycleOwner){
            AppLogger.errorMessage(TAG,it)
        }
        viewModel.files.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    AppLogger.errorMessage(TAG, it.data?.size.toString())
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        var bucketId = p0?.getTag() as Int
        viewModel.bucketId.value=bucketId.toString()
        NavHostFragment.findNavController(this).navigate(R.id.media_file_list)
        AppLogger.errorMessage(TAG, "bucketId")

    }


}