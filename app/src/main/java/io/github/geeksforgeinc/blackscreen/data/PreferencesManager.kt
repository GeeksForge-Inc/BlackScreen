package io.github.geeksforgeinc.blackscreen.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        private const val BLACK_SCREEN_DATA_STORE_FILE_NAME = "black_screen_data_store"
        val FLOAT_SERVICE_ENABLED_KEY = booleanPreferencesKey("float_service_enabled")
    }
    private val dataStore : DataStore<Preferences> =  PreferenceDataStoreFactory.create(
    produceFile = {
        context.preferencesDataStoreFile(BLACK_SCREEN_DATA_STORE_FILE_NAME)
    })

    fun isFloatServiceEnabled() : Flow<Boolean> {
       return dataStore.data.map { preferences ->
            preferences[FLOAT_SERVICE_ENABLED_KEY] ?: false
        }
    }

    suspend fun setFloatServiceEnabled(isFloatServiceEnabled: Boolean) {
      dataStore.edit { preferences ->
              preferences[FLOAT_SERVICE_ENABLED_KEY] = isFloatServiceEnabled
      }
    }

}