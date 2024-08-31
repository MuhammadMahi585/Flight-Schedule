package com.example.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

//airport dao
@Dao
interface AirportDao{

    @Query("SELECT * FROM airport WHERE name LIKE '%' || :name || '%' OR iata_code LIKE '%' || :name || '%'")
    fun getAutoCompleteList(name:String):Flow<List<airport>>

    @Query("SELECT * FROM airport where iata_code!=:Iata")
    fun getListExceptSearched(Iata:String):Flow<List<airport>>
}

//favorite dao
@Dao
interface FavoriteDao{
     @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insertFavoritePlace(favorite: favorite)

     @Query("Select * From favorite")
     fun getFavoriteFlights():Flow<List<favorite>>
}