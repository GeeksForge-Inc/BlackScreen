package io.github.geeksforgeinc.blackscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.geeksforgeinc.blackscreen.data.PreferencesManager
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlackScreenViewModel @Inject constructor(private val preferencesManager : PreferencesManager) : ViewModel() {
    val floatServiceEnabledLiveData = preferencesManager.isFloatServiceEnabled().asLiveData()

    fun setFloatServiceEnabled(isFloatServiceEnabled : Boolean) = viewModelScope.launch {
        preferencesManager.setFloatServiceEnabled(isFloatServiceEnabled)
    }
}