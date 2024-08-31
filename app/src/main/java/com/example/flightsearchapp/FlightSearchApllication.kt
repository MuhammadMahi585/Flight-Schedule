package com.example.flightsearchapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearchapp.data.Container
import com.example.flightsearchapp.data.DefaultAppContainer
import com.example.flightsearchapp.data.UserPreferenceRepository


private const val name = "is_name"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = name
)
class FlightSearchApplication:Application() {
    lateinit var userPreferencesRepository: UserPreferenceRepository
    lateinit var container:Container
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        userPreferencesRepository = UserPreferenceRepository(dataStore = dataStore)
    }
}