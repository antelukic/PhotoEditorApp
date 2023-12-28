package com.photoeditor.app.presentation.home

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mukesh.imageproccessing.OnProcessingCompletionListener
import com.mukesh.imageproccessing.PhotoFilter
import com.mukesh.imageproccessing.filters.AutoFix
import com.mukesh.imageproccessing.filters.Brightness
import com.mukesh.imageproccessing.filters.Contrast
import com.mukesh.imageproccessing.filters.CrossProcess
import com.mukesh.imageproccessing.filters.Documentary
import com.mukesh.imageproccessing.filters.DuoTone
import com.mukesh.imageproccessing.filters.FillLight
import com.mukesh.imageproccessing.filters.FishEye
import com.mukesh.imageproccessing.filters.FlipHorizontally
import com.mukesh.imageproccessing.filters.FlipVertically
import com.mukesh.imageproccessing.filters.Grain
import com.mukesh.imageproccessing.filters.Grayscale
import com.mukesh.imageproccessing.filters.Highlight
import com.mukesh.imageproccessing.filters.Lomoish
import com.mukesh.imageproccessing.filters.Negative
import com.mukesh.imageproccessing.filters.None
import com.mukesh.imageproccessing.filters.Posterize
import com.mukesh.imageproccessing.filters.Rotate
import com.mukesh.imageproccessing.filters.Saturate
import com.mukesh.imageproccessing.filters.Sepia
import com.mukesh.imageproccessing.filters.Sharpen
import com.mukesh.imageproccessing.filters.Temperature
import com.mukesh.imageproccessing.filters.Tint
import com.mukesh.imageproccessing.filters.Vignette
import com.photoeditor.app.R
import com.photoeditor.app.databinding.FragmentHomeBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Date

class HomeFragment : Fragment(), OnFilterClickListener, OnProcessingCompletionListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding by lazy { _binding!! }
    private lateinit var result: Bitmap
    private var photoFilter: PhotoFilter? = null

    override fun onProcessingComplete(bitmap: Bitmap) {
        result = bitmap
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(LayoutInflater.from(context), container, false)
        binding.setUpUI()
        return binding.root
    }

    override fun onFilterClicked(effectsThumbnail: EffectsThumbnail) {
        ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background)?.toBitmap()?.let { bitmap ->
            photoFilter?.applyEffect(bitmap, effectsThumbnail.filter)
        }
    }

    private fun FragmentHomeBinding.setUpUI() {
        setPhotoFilter()
        effectsRecyclerView.setEffectsRV()
        setClicks()
    }

    private fun FragmentHomeBinding.setPhotoFilter() {
        photoFilter = PhotoFilter(effectView, this@HomeFragment)
        ContextCompat.getDrawable(root.context, R.drawable.ic_launcher_background)?.toBitmap()?.let { bitmap ->
            photoFilter?.applyEffect(bitmap, None())
        }
    }

    private fun RecyclerView.setEffectsRV() {
        layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        setHasFixedSize(true)
        adapter = EffectsAdapter(this@HomeFragment).also {
            it.submitList(getItems())
        }
    }

    private fun FragmentHomeBinding.setClicks() {
        saveButton.bringToFront()
        saveButton.setOnClickListener {
            checkPermissionAndSaveImage()
        }
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
        result.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
        fOut.flush()
        fOut.close()
        MediaStore.Images.Media.insertImage(context?.contentResolver, file.absolutePath, file.name, file.name)
        Toast.makeText(context, "ImageSaved", Toast.LENGTH_SHORT)
            .show()
    }

    private fun getItems(): MutableList<EffectsThumbnail> {
        return mutableListOf(
            EffectsThumbnail("None", None()),
            EffectsThumbnail("AutoFix", AutoFix()),
            EffectsThumbnail("Highlight", Highlight()),
            EffectsThumbnail("Brightness", Brightness()),
            EffectsThumbnail("Contrast", Contrast()),
            EffectsThumbnail("Cross Process", CrossProcess()),
            EffectsThumbnail("Documentary", Documentary()),
            EffectsThumbnail("Duo Tone", DuoTone()),
            EffectsThumbnail("Fill Light", FillLight()),
            EffectsThumbnail("Fisheye", FishEye()),
            EffectsThumbnail("Flip Horizontally", FlipHorizontally()),
            EffectsThumbnail("Flip Vertically", FlipVertically()),
            EffectsThumbnail("Grain", Grain()),
            EffectsThumbnail("Grayscale", Grayscale()),
            EffectsThumbnail("Lomoish", Lomoish()),
            EffectsThumbnail("Negative", Negative()),
            EffectsThumbnail("Posterize", Posterize()),
            EffectsThumbnail("Rotate", Rotate()),
            EffectsThumbnail("Saturate", Saturate()),
            EffectsThumbnail("Sepia", Sepia()),
            EffectsThumbnail("Sharpen", Sharpen()),
            EffectsThumbnail("Temperature", Temperature()),
            EffectsThumbnail("Tint", Tint()),
            EffectsThumbnail("Vignette", Vignette())
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val REQUEST_PERMISSION: Int = 10001
    }
}
