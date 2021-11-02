package io.github.geeksforgeinc.blackscreen.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.geeksforgeinc.blackscreen.service.FloatServiceManager
import javax.inject.Inject

@HiltViewModel
class BlackScreenViewModel @Inject constructor(private val floatServiceManager: FloatServiceManager) : ViewModel() {
    val floatServiceEnabledLiveData = floatServiceManager.serviceStatusLiveData

    fun setFloatServiceEnabled(isFloatServiceEnabled : Boolean) = floatServiceManager.updateServiceStatus(isFloatServiceEnabled)
}