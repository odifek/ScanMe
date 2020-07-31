package com.odifek.scanme.text

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.math.min

class ScanTextViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    val picturePath: MutableLiveData<String?> = savedStateHandle.getLiveData("picturePath")

    fun decodeBitmap(targetWidth: Int, targetHeight: Int): LiveData<Bitmap> = liveData {
        val photoPath = picturePath.value ?: return@liveData
        val bitmapOptions = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            val photoWidth = outWidth
            val photoHeight = outHeight

            val scaleFactor = min(photoWidth / targetWidth, photoHeight / targetHeight)

            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }

        withContext(Dispatchers.Default) {
            BitmapFactory.decodeFile(photoPath, bitmapOptions).also { bitmap: Bitmap? ->

                if (bitmap != null) bitmap.also {
                    withContext(Dispatchers.Main) {
                        emit(it)
                    }
                } else {
                    Timber.w("Failed to decode picture!")
                }
            }
        }
    }
}