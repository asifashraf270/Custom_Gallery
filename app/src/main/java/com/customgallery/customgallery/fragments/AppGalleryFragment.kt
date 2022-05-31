package com.customgallery.customgallery.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.customgallery.utilities.PermissionUtils
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
        activity?.let {
            when {
                PermissionUtils.isExternalStoragePermission(activity!!) -> {
                    AppLogger.errorMessage(TAG, "Permission Granted")
                    if (viewModel.loadAllBuckets.value == null)
                        viewModel.loadAllBuckets.value = ""

                }
                PermissionUtils.shouldShowRationale(activity!!) -> {
                    AppLogger.errorMessage(TAG, "Open Setting")
                    Toast.makeText(
                        activity,
                        "Please open setting and allow Permission to Proceed",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    AppLogger.errorMessage(TAG, "Request Permission")
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        10000
                    )
                }

            }
        }
        setUpAdapter()
        eventObserverListener()
    }

    fun setUpAdapter() {
        adapter = AppGalleryAdapter()
        binding.appGallery.layoutManager = GridLayoutManager(context, 2)
        binding.appGallery.adapter = adapter
        adapter.onclickListener = this
    }

    override fun onDestroy() {
        super.onDestroy()
        AppLogger.errorMessage(TAG, "onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        AppLogger.errorMessage(TAG, "onDestroyView")
    }

    fun eventObserverListener() {

        viewModel.allBuckets.observe(viewLifecycleOwner) {
            if (it == null) return@observe
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
        viewModel.bucketId.observe(viewLifecycleOwner) {
            AppLogger.errorMessage(TAG, it)
        }

    }

    override fun onClick(p0: View?) {
        var position = p0?.getTag() as Int
        viewModel.fileType.value = viewModel.allBuckets.value?.data?.get(position)?.fileType
        viewModel.bucketId.value = viewModel.allBuckets.value?.data?.get(position)?.id.toString()
        NavHostFragment.findNavController(this).navigate(R.id.media_file_list)
        AppLogger.errorMessage(TAG, "bucketId")

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == 10000) {
            if (permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is granted, do your work
                if (viewModel.loadAllBuckets.value == null)
                    viewModel.loadAllBuckets.value = ""

                AppLogger.errorMessage(TAG, "Permission Granted")
            }
        }
    }


}