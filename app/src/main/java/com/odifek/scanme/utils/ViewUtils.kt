package com.odifek.scanme.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.savedstate.SavedStateRegistryOwner
import timber.log.Timber

fun NavController.safeNavigate(navDirections: NavDirections) {
    try {
        navigate(navDirections)
    } catch (e: Exception) {
        Timber.w(e)
    }
}

inline fun <reified T : ViewModel> Fragment.savedStateViewModel(
    crossinline creator: (SavedStateHandle) -> T
): Lazy<T> {

    return createViewModelLazy(T::class, storeProducer = {
        viewModelStore
    }, factoryProducer = {
        createAbstractSavedStateViewModelFactory(
            arguments = arguments ?: Bundle(), creator = creator
        )
    })
}

inline fun <reified T : ViewModel> SavedStateRegistryOwner.createAbstractSavedStateViewModelFactory(
    arguments: Bundle,
    crossinline creator: (SavedStateHandle) -> T
): ViewModelProvider.Factory {
    return object : AbstractSavedStateViewModelFactory(this, arguments) {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(
            key: String, modelClass: Class<T>, handle: SavedStateHandle
        ): T = creator(handle) as T
    }
}