package com.example.flightsearchapp.data

import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface Repository{
    fun getAutoCompleteList(name:String) :Flow<List<airport>>

    fun getListExceptSearched(Iata:String):Flow<List<airport>>

    fun getFavoriteFlights():Flow<List<favorite>>

    suspend fun insertFavoritePlace(favorite: favorite)
}
class ImplementRepository(
    private val airportDao: AirportDao,
    private val favoriteDao: FavoriteDao):Repository{
    override fun getAutoCompleteList(name:String) :Flow<List<airport>> =airportDao.getAutoCompleteList(name)

    override fun getListExceptSearched(Iata:String):Flow<List<airport>> = airportDao.getListExceptSearched(Iata)

    override fun getFavoriteFlights():Flow<List<favorite>> = favoriteDao.getFavoriteFlights()

    override suspend fun insertFavoritePlace(favorite: favorite) = favoriteDao.insertFavoritePlace(favorite)
}