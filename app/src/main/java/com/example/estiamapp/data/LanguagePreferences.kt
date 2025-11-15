package com.example.estiamapp.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class LanguagePreferences(private val context: Context) {

    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("selected_language")
        const val FRENCH = "fr"
        const val ENGLISH = "en"
    }

    val selectedLanguage: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_KEY] ?: FRENCH
        }

    suspend fun setLanguage(languageCode: String) {
        Log.d("LanguagePreferences", "Setting language to: $languageCode")
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
    }
}