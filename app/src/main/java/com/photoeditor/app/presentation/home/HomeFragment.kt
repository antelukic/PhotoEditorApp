package com.photoeditor.app.presentation.home

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.photoeditor.app.R
import com.photoeditor.app.databinding.FragmentHomeBinding
import com.photoeditor.app.ext.getBitmap
import com.photoeditor.app.ext.safelyNavigate
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding by lazy { _binding!! }

    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(LayoutInflater.from(context), container, false)
        binding.setUpUI()
        return binding.root
    }

    private fun FragmentHomeBinding.setUpUI() {
        setClicks()
    }

    private fun FragmentHomeBinding.setClicks() {
        btnOpenGallery.setOnClickListener(openGalleryClickListener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            btnOpenCamera.setOnClickListener(openCameraClickListener)
        }
    }

    private val openGalleryClickListener = View.OnClickListener {
        Intent().also {
            it.type = GALLERY_INTENT_TYPE
            it.action = Intent.ACTION_GET_CONTENT
            resultGalleryLauncher.launch(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val openCameraClickListener = View.OnClickListener {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            resultCameraLauncher.launch(takePictureIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "takePicture: ERROR ${e.message}")
        }
    }

    private val resultGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data?.data != null) {
                    result.data?.data?.let { imageUri ->
                        imageUri.publishImage()
                        navigateToEditPhotoScreen()
                    }
                } else {
                    Log.e(TAG, "resultGalleryLauncher: $result")
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }

    private val resultCameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val extraData = if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
                    data?.extras?.getParcelable("data", Bitmap::class.java)
                } else {
                    data?.extras?.get("data") as? Bitmap
                }
                extraData?.let(viewModel::publishPickedImage)
                navigateToEditPhotoScreen()
            } else {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
            }
        }

    private fun Uri?.publishImage() {
        this?.getBitmap(requireContext().contentResolver)?.let { bitmap ->
            viewModel.publishPickedImage(bitmap)
        }
    }

    private fun navigateToEditPhotoScreen() {
        findNavController().safelyNavigate(R.id.action_homeFragment_to_addFilterFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
        private const val GALLERY_INTENT_TYPE = "image/*"
    }
}
