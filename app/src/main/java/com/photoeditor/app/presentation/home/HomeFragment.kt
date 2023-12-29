package com.photoeditor.app.presentation.home

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.photoeditor.app.R
import com.photoeditor.app.databinding.FragmentHomeBinding
import com.photoeditor.app.ext.getBitmap
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
        btnOpenCamera.setOnClickListener(openCameraClickListener)
    }

    private val openGalleryClickListener = View.OnClickListener {
        Intent().also {
            it.type = GALLERY_INTENT_TYPE
            it.action = Intent.ACTION_GET_CONTENT
            resultGalleryLauncher.launch(it)
        }
    }

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
                        result?.data?.data.publishImage()
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
                data?.data.publishImage()
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
        findNavController().navigate(R.id.action_homeFragment_to_editPhotoFragment)
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
