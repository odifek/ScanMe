package com.odifek.scanme.text

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MLScanText @Inject constructor(private val textRecognizer: TextRecognizer) {

    suspend fun extractTextFromBitMap(bitmap: Bitmap, rotation: Int): List<String> =
        suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, rotation)
            textRecognizer.process(image)
                .addOnSuccessListener { visionText ->
                    Timber.d(visionText.text)
                    val lines =
                        visionText.textBlocks.flatMap { textBlock -> textBlock.lines.map { it.text } }
                    continuation.resume(lines)
                }
                .addOnFailureListener { exception ->
                    Timber.w(exception)
                    continuation.resumeWithException(exception)
                }
        }
}