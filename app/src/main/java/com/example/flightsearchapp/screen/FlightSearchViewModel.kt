package com.example.flightsearchapp.screen


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearchapp.FlightSearchApplication
import com.example.flightsearchapp.data.Repository
import com.example.flightsearchapp.data.UserPreferenceRepository
import com.example.flightsearchapp.data.airport
import com.example.flightsearchapp.data.favorite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface DisplayControl {
    object favortite:DisplayControl
    object EmptySearch:DisplayControl
}

class FlightSearchViewModel(
   private val repository:Repository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {
    var nameString: StateFlow<String> = userPreferenceRepository.isName
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )
    var name by mutableStateOf(nameString.value)

    var screen:DisplayControl by mutableStateOf(DisplayControl.favortite)

    val _uistate = MutableStateFlow(UiState())

    var favoriteUiState by  mutableStateOf(UiState())
     init {
        viewModelScope.launch {
            userPreferenceRepository.isName.collect { storedName ->
                name = storedName
                updateScreenState()
            }
        }
    }
    fun getList(nameORIATA:String):Flow<List<airport>> =  repository.getAutoCompleteList(name = nameORIATA)

    fun getShortedList(nameS:String):Flow<List<airport>> = repository.getListExceptSearched(nameS)

    fun addToFavorite(departure_code: String,destination_code: String) {
       _uistate.value = _uistate.value.copy(
           departure_code = departure_code,
           destination_code = destination_code
       )
        favoriteUiState = UiState(
            departure_code = departure_code,
            destination_code = destination_code
        )
    }
    fun addFav() {
        viewModelScope.launch {
            repository.insertFavoritePlace(favoriteUiState.toFavorite())
        }
    }
    fun getFavorite():Flow<List<favorite>> = repository.getFavoriteFlights()
    fun onNameChange(newName: String) {
        nameSet(newName)
        name = newName
        updateScreenState()
    }

    private fun updateScreenState() {
        screen = if (name.isEmpty()) {
            DisplayControl.favortite
        } else {
            DisplayControl.EmptySearch
        }
    }

    fun nameSet(name:String){
        viewModelScope.launch {
            userPreferenceRepository.saveName(name)
        }
    }
    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
          initializer {
              val application = this[APPLICATION_KEY] as FlightSearchApplication
              FlightSearchViewModel(application.container.repository,application.userPreferencesRepository)
          }
        }
        }
    }
data class UiState(
    val id:Int=0,
    val departure_code:String="",
    val destination_code:String=""
)
fun UiState.toFavorite():favorite = favorite(
     id = id,
     departure_code = departure_code,
    destination_code = destination_code
)