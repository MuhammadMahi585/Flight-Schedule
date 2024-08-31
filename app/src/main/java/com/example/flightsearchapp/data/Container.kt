package com.example.flightsearchapp.data

import android.content.Context

interface Container {
    val repository:Repository
}
class DefaultAppContainer(private val context:Context):Container{
    override val repository:Repository by lazy {
      ImplementRepository(AirportDB.getDatabase(context).airportDao(),AirportDB.getDatabase(context).favorite())
    }
}