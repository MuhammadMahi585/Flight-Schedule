package com.example.flightsearchapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flightsearchapp.screen.FlightSearchViewModel
import com.example.flightsearchapp.screen.MainScreen
import com.example.flightsearchapp.screen.SelectedDestinationList
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
     object MainScreen : Screen("main_screen")
     object SelectedDestinationList : Screen("selected_destination_list/{name}/{iata}") {
          fun createRoute(name: String, iata: String) = "selected_destination_list/$name/$iata"
     }
}
@Composable
fun NavigationF() {
     val navController = rememberNavController()
     val courtineScope = rememberCoroutineScope()
     val viewModel:FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.factory)
     NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
          composable(Screen.MainScreen.route) {
               MainScreen(viewModel = viewModel, navController = navController)
          }
          composable(
               route = Screen.SelectedDestinationList.route,
               arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("iata") { type = NavType.StringType }
               )
          ) { backStackEntry ->
               val name = backStackEntry.arguments?.getString("name") ?: ""
               val iata = backStackEntry.arguments?.getString("iata") ?: ""
               SelectedDestinationList(
                    viewModel = viewModel,
                    name = name,
                    iata = iata,
                    onAdd = {viewModel.addFav()},
                    selectedAirports = viewModel.getShortedList(iata).collectAsState(emptyList()).value
               )
          }
     }
}