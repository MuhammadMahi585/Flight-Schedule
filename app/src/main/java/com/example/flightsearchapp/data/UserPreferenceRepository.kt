package com.example.flightsearchapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val name = stringPreferencesKey("is_name")
    }
    suspend fun saveName(nameEntered:String){
       dataStore.edit {
           it[name]=nameEntered
       }
    }
    val isName: Flow<String> = dataStore.data
        .map {preference->
            preference[name] ?:""
        }
}