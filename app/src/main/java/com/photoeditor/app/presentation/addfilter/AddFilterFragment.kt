package com.photoeditor.app.presentation.addfilter

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.devs.sketchimage.SketchImage
import com.google.android.material.slider.Slider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.photoeditor.app.databinding.FragmentAddFilterBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Date
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFilterFragment : Fragment() {

    private var _binding: FragmentAddFilterBinding? = null
    private val binding: FragmentAddFilterBinding by lazy { _binding!! }
    private val viewModel by viewModel<AddFilterViewModel>()
    private var sketchImage: SketchImage? = null
    private var tabEditedImage: Bitmap? = null

    private val resetBtnClickListener = View.OnClickListener {
        lifecycleScope.launch {
            viewModel.filteredImage.emit(null)
        }
        binding.imgPhotoEdited.setImageBitmap(viewModel.originalImage.value)
        viewModel.tabProgress.clear()
        binding.valueSlider.value = 0f
    }

    private val onSliderValueChangeListener = Slider.OnChangeListener { _, value, _ ->
        lifecycleScope.launch {
            val sliderValue = (value * 100).toInt()
            if (sketchImage == null) return@launch
            tabEditedImage = sketchImage?.getImageAs(viewModel.selectedTab, sliderValue)
            viewModel.filteredImage.emit(tabEditedImage)
            viewModel.tabProgress.remove(viewModel.selectedTab)
            viewModel.tabProgress[viewModel.selectedTab] = sliderValue
        }
    }

    private val tabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            viewModel.selectedTab = when (tab?.position) {
                0 -> SketchImage.ORIGINAL_TO_GRAY
                1 -> SketchImage.ORIGINAL_TO_COLORED_SKETCH
                2 -> SketchImage.ORIGINAL_TO_SOFT_SKETCH
                3 -> SketchImage.ORIGINAL_TO_SOFT_COLOR_SKETCH
                4 -> SketchImage.GRAY_TO_SKETCH
                5 -> SketchImage.GRAY_TO_COLORED_SKETCH
                6 -> SketchImage.GRAY_TO_SOFT_SKETCH
                7 -> SketchImage.GRAY_TO_SOFT_COLOR_SKETCH
                8 -> SketchImage.SKETCH_TO_COLOR_SKETCH
                else -> SketchImage.ORIGINAL_TO_GRAY
            }
            sketchImage = SketchImage.Builder(context, tabEditedImage).build()
            binding.valueSlider.value = (viewModel.tabProgress[viewModel.selectedTab]?.toFloat()?.div(100)) ?: 0.0f
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddFilterBinding.inflate(LayoutInflater.from(context), container, false)
        binding.setUpObservers()
        return binding.root
    }

    private fun FragmentAddFilterBinding.setUpObservers() {
        lifecycleScope.launch {
            viewModel.originalImage.collectLatest { bitmap: Bitmap? ->
                bitmap?.let {
                    setUpUI(bitmap)
                    setClicks()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.filteredImage
                .collectLatest { bitmap: Bitmap? ->
                    bitmap?.let {
                        imgPhotoEdited.setImageBitmap(it)
                    }
                }
        }
    }

    private fun FragmentAddFilterBinding.setUpUI(image: Bitmap) {
        tabs.addOnTabSelectedListener(tabSelectedListener)
        valueSlider.addOnChangeListener(onSliderValueChangeListener)
        imgPhotoOriginal.setImageBitmap(image)
        imgPhotoEdited.setImageBitmap(image)
        sketchImage = SketchImage.Builder(context, image).build()
    }

    private fun FragmentAddFilterBinding.setClicks() {
        btnApply.setOnClickListener {
            viewModel.filteredImage.value?.let(viewModel::publishFilteredImage)
        }
        btnReset.setOnClickListener(resetBtnClickListener)
    }

    private fun checkPermissionAndSaveImage() {
        if (checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_PERMISSION)
        } else {
            saveImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage()
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }
        }
    }

    private fun saveImage() {
        val path = Environment.getExternalStorageDirectory()
            .toString()
        val fOut: OutputStream?
        val fileName = Date().time
        val file = File(path, "$fileName.jpg")
        fOut = FileOutputStream(file)
        fOut.flush()
        fOut.close()
        MediaStore.Images.Media.insertImage(context?.contentResolver, file.absolutePath, file.name, file.name)
        Toast.makeText(context, "ImageSaved", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.tabs.clearOnTabSelectedListeners()
        _binding = null
    }

    companion object {
        private const val REQUEST_PERMISSION: Int = 10001
    }
}
