package io.github.geeksforgeinc.blackscreen.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FloatServiceManager @Inject constructor() {
    private val _serviceStatusLiveData = MutableLiveData(false)
    val serviceStatusLiveData : LiveData<Boolean> = _serviceStatusLiveData

    fun updateServiceStatus(isServiceRunning : Boolean) {
        _serviceStatusLiveData.value = isServiceRunning
    }
}