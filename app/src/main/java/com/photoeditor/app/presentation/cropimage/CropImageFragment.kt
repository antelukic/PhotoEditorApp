package com.photoeditor.app.presentation.cropimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.photoeditor.app.databinding.FragmentCropImageBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CropImageFragment : Fragment() {

    private var _binding: FragmentCropImageBinding? = null
    private val binding: FragmentCropImageBinding by lazy { _binding!! }
    private val viewModel by viewModel<CropImageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCropImageBinding.inflate(LayoutInflater.from(context), container, false)
        binding.setObservers()
        return binding.root
    }

    private fun FragmentCropImageBinding.setObservers() {
        lifecycleScope.launch {
            viewModel.image.collectLatest { image ->
                kropView.setBitmap(image)
            }
        }

        btnCrop.setOnClickListener {
            kropView.getCroppedBitmap()?.let(viewModel::publishCroppedImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
