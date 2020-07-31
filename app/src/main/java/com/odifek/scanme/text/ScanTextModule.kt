package com.odifek.scanme.text

import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import dagger.Module
import dagger.Provides

@Module
object ScanTextModule {

    @Provides
    fun providesTextRecognizer(): TextRecognizer = TextRecognition.getClient()

}