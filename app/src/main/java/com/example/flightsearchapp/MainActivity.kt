package com.example.flightsearchapp

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.flightsearchapp.navigation.NavigationF
import com.example.flightsearchapp.screen.FlightSearchViewModel
import com.example.flightsearchapp.screen.MainScreen
import com.example.flightsearchapp.ui.theme.FlightSearchAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Initialize ViewModel
        setContent {
            FlightSearchAppTheme {
                NavigationF()
            }
        }
    }
}

