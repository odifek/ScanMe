package com.odifek.scanme.text

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import com.odifek.scanme.R
import com.odifek.scanme.databinding.FragmentScanTextBinding
import timber.log.Timber

class ScanTextFragment : Fragment(R.layout.fragment_scan_text) {

    private var _binding: FragmentScanTextBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentScanTextBinding.bind(view)


        binding.buttonScannow.setOnClickListener {
            launchCamera()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { cameraIntent ->
            cameraIntent.resolveActivity(requireActivity().packageManager)?.also {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitMap = data?.extras?.get("data") as? Bitmap
            Timber.d("Image size: ${imageBitMap?.width} x ${imageBitMap?.height}")
            imageBitMap?.also { binding.imageViewResult.setImageBitmap(it) }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 100
    }
}