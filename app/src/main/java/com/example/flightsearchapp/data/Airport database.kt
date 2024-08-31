package com.example.flightsearchapp.data

import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Entity
import androidx.room.PrimaryKey

//airport Entity
@Entity(tableName = "airport")
data class airport(
    @PrimaryKey
    val id:Int,
    val name:String,
    val iata_code:String,
    val passengers:Int
)

//favorite Entity
@Entity(tableName = "favorite")
data class favorite(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val departure_code:String,
    val destination_code:String
)
