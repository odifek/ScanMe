package com.odifek.scanme.text

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.math.min

class ScanTextViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val mlScanText: MLScanText
) : ViewModel() {

    val picturePath: MutableLiveData<String?> = savedStateHandle.getLiveData("picturePath")

    val decodedText: MutableLiveData<String> = savedStateHandle.getLiveData("decodedText")

    fun displayBitmap(targetWidth: Int, targetHeight: Int): LiveData<Bitmap> = liveData {
       val bitmap = decodeBitmap(targetWidth, targetHeight) ?: return@liveData
        emit(bitmap)
    }

    private suspend fun decodeBitmap(targetWidth: Int, targetHeight: Int): Bitmap? {
        val photoPath = picturePath.value ?: return null
        val bitmapOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            val photoWidth = outWidth
            val photoHeight = outHeight

            val scaleFactor = min(photoWidth / targetWidth, photoHeight / targetHeight)

            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }

        return withContext(Dispatchers.Default) {
            BitmapFactory.decodeFile(photoPath, bitmapOptions)
        }
    }

    fun decodeTextInImage() {
        viewModelScope.launch {
            val bitmap = decodeBitmap(640, 480)
            if (bitmap != null) {
                val result = mlScanText.extractTextFromBitMap(bitmap, 0)
                decodedText.value = result.joinToString()
            } else {
                Timber.w("Failed to decode bitmap!")
            }
        }
    }
}