package com.odifek.scanme.text

import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object ScanTextModule {

    @Provides
    fun providesTextRecognizer(): TextRecognizer = TextRecognition.getClient()

}