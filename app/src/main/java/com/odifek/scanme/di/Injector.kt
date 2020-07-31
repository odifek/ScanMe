package com.odifek.scanme.di

import android.content.Context

object Injector {

    private var appComponent: AppComponent? = null

    fun init(context: Context) {
        appComponent = DaggerAppComponent.factory().create(context)
    }

    fun get(): AppComponent = appComponent!!
}