package com.hamidrezabashiri.ezcard.ui.screens.signup

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val dataStore: DataStore<Preferences>) :
    ViewModel() {

    private val isFirstLoginKey = booleanPreferencesKey("is_first_login")


    fun onSignUpButtonClicked() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[isFirstLoginKey] = false
            }
        }
    }

}