package com.customgallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.customgallery.customgallery.MediaViewModel
import com.customgallery.utilities.AppLogger
import com.customgallery.utilities.Status
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MediaViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    override fun onBackPressed() {
        if (findNavController(R.id.app_gallery_nav_activity).popBackStack().not()) {
            super.onBackPressed()
        }
    }
}