package com.odifek.scanme.di

import android.content.Context
import com.odifek.scanme.text.ScanTextModule
import com.odifek.scanme.utils.ScanFileUtils
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ScanTextModule::class])
interface AppComponent {
    fun fileUtils(): ScanFileUtils

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}