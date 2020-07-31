package com.odifek.scanme.text

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.odifek.scanme.R
import com.odifek.scanme.databinding.FragmentScanTextBinding
import com.odifek.scanme.utils.ScanFileUtils
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ScanTextFragment : Fragment(R.layout.fragment_scan_text) {

    private val viewModel: ScanTextViewModel by viewModels()
    @Inject
    lateinit var fileUtils: ScanFileUtils
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
                val pictureFile = try {
                    fileUtils.createImageFile()
                } catch (e: Exception) {
                    Timber.w(e)
                    null
                }
                pictureFile?.also {
                    val pictureUri = FileProvider.getUriForFile(
                        requireContext(),
                        ScanFileUtils.FILE_PROVIDER_AUTHORITY,
                        it
                    )
                    viewModel.picturePath.value = it.absolutePath
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri)
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            viewModel.decodeBitmap(binding.imageViewResult.width, binding.imageViewResult.height)
                .observe(viewLifecycleOwner) { bitmap ->
                    binding.imageViewResult.setImageBitmap(bitmap)
                }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 100
    }
}