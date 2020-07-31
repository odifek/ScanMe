package com.odifek.scanme

import android.app.Application
import com.odifek.scanme.di.Injector

class ScanApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }
}