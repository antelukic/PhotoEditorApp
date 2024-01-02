package com.photoeditor.app.presentation.paintimage

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.photoeditor.app.databinding.FragmentPaintBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaintFragment : Fragment() {

    private var _binding: FragmentPaintBinding? = null
    private val binding: FragmentPaintBinding by lazy { _binding!! }
    private val viewModel by viewModel<PaintViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaintBinding.inflate(LayoutInflater.from(context), container, false)
        binding.setUpObservers()
        binding.setClicks()
        return binding.root
    }

    private fun FragmentPaintBinding.setUpObservers() {
        lifecycleScope.launch {
            viewModel.originalImage.collectLatest {
                it?.let { bitmap ->
                    setUpUI(bitmap)
                }
            }
        }
    }

    private fun FragmentPaintBinding.setUpUI(image: Bitmap) {
        finger.setImageBitmap(image)
    }

    private fun FragmentPaintBinding.setClicks() {
        emboss.setOnClickListener { finger.emboss() }
        normal.setOnClickListener { finger.normal() }
        blur.setOnClickListener { finger.blur() }
        clear.setOnClickListener { finger.clear() }
        save.setOnClickListener { finger.drawable?.toBitmap()?.let { it1 -> viewModel.publishPaintedImage(it1) } }
        red.setOnSeekBarChangeListener(seekBarChangeListener)
        green.setOnSeekBarChangeListener(seekBarChangeListener)
        blue.setOnSeekBarChangeListener(seekBarChangeListener)
        tolerance.setOnSeekBarChangeListener(seekBarChangeListener)
        width.setOnSeekBarChangeListener(seekBarChangeListener)
    }

    private val seekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            when (seekBar?.id) {
                binding.red.id, binding.green.id, binding.blue.id -> {
                    val r = binding.red.progress
                    val g = binding.green.progress
                    val b = binding.blue.progress
                    val color = Color.argb(255, r, g, b)
                    binding.finger.strokeColor = color
                    binding.imgColorPreview.setBackgroundColor(color)
                }
                binding.tolerance.id -> {
                    binding.finger.touchTolerance = progress.toFloat()
                }
                binding.width.id -> {
                    binding.finger.strokeWidth = progress.toFloat()
                }
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            // no op
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            // no op
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
