package com.example.flightsearchapp

import android.app.Application
import com.example.flightsearchapp.data.Container
import com.example.flightsearchapp.data.DefaultAppContainer


class FlightSearchApplication:Application() {
    lateinit var container:Container
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}