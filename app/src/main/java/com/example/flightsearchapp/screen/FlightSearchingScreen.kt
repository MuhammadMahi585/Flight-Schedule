package com.example.flightsearchapp.screen


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flightsearchapp.R
import com.example.flightsearchapp.data.airport
import com.example.flightsearchapp.data.favorite
import com.example.flightsearchapp.navigation.Screen
import com.example.flightsearchapp.ui.theme.FlightSearchAppTheme
import com.example.flightsearchapp.ui.theme.primaryDark

@Composable
fun MainScreen(
    viewModel:FlightSearchViewModel= viewModel(factory = FlightSearchViewModel.factory),
    displayControl: DisplayControl = viewModel.screen,
    navController: NavController
    ){
     Surface(
         modifier = Modifier
             .fillMaxSize()

     ) {
        Column {
             SearchTopAppBar(
                 title = stringResource(R.string.app_title),
                 modifier = Modifier,
                 style = MaterialTheme.typography.headlineLarge
             )
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = {viewModel.onNameChange(it) },
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "null") },
                trailingIcon = {
                    Image(painter = painterResource(id = R.drawable.baseline_keyboard_voice_24), contentDescription = "null")
                },
                        placeholder = {Text(text = "Enter departure airport")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(id = R.dimen.large_padding),
                                start = dimensionResource(id = R.dimen.medium_padding),
                                end = dimensionResource(id = R.dimen.medium_padding),
                                bottom = dimensionResource(id = R.dimen.minor_padding)
                            ))
            val fav by viewModel.getFavorite().collectAsState(initial = emptyList())
            val routeSchedule by viewModel.getList(viewModel.name).collectAsState(initial = emptyList())
            when(displayControl){
                is DisplayControl.favortite ->    ViewFavorites(favorites = fav)
                is DisplayControl.EmptySearch->    AutoCompleteList(airports = routeSchedule,navController)
            }
                }
         }
     }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(title:String,modifier: Modifier,
                    style:TextStyle){
    TopAppBar(
        modifier = modifier,
        title = {
    Text(text = title,
        style = style)},
        colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
            containerColor = primaryDark,
        ))
}
@Composable
fun AutoCompleteList(
    airports: List<airport>,
    navController: NavController
){
        LazyColumn(
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.no_padding))
        ) {
            items(airports) { airport ->
                DisplayAirportCard(airport, navController = navController)
            }
        }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayAirportCard(
    airport: airport,
    navController: NavController
){
    Card(onClick = {
        navController.navigate(
            Screen.SelectedDestinationList.createRoute(airport.name, airport.iata_code)
        )
    },
        modifier = Modifier.fillMaxWidth()) {
       Row(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_padding))) {
           Text(
               text = airport.iata_code+" - "+airport.name,
               style = MaterialTheme.typography.titleMedium)
       }
    }
}
@Composable
fun SelectedDestinationList(
    viewModel: FlightSearchViewModel,
    onAdd:()->Unit,
    name:String,
    iata:String,
    selectedAirports:List<airport>
) {
    Column {
        SearchTopAppBar(
            title = stringResource(R.string.app_title),
            modifier = Modifier,
            style = MaterialTheme.typography.headlineLarge
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.medium_padding))
        ) {
            Text(
                text = "Flights From $iata",
                style = MaterialTheme.typography.titleLarge
            )
            LazyColumn {
                items(selectedAirports) { airport ->
                    SelectedListCard(
                        airport = airport,
                        OnAdd = onAdd,
                        viewModel = viewModel,
                        name = name,
                        iata = iata
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedListCard(
    airport: airport,
    OnAdd:()->Unit,
    viewModel: FlightSearchViewModel,
    name:String,
    iata:String) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.small_padding))
    ){
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (
                modifier = Modifier
                    .weight(3f)
            )
            {
                Text(
                    text = "Depart",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.minor_padding))
                )
                Row (
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "${iata} ",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "${name}",
                        style = MaterialTheme.typography.titleMedium
                    )

                }
                Text(
                    text = "Destination",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.minor_padding))
                )
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "${airport.iata_code}  ",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "${airport.name}",
                        style = MaterialTheme.typography.titleMedium
                    )

                }
            }
            var addedButton by rememberSaveable {
                mutableStateOf(false)
            }
            IconButton(onClick = {addedButton=!addedButton
                viewModel.addToFavorite(iata,airport.iata_code)
                OnAdd()
            }) {
                if(addedButton)
                Icon(
                    imageVector = Icons.Default.Star, contentDescription = "Add to Favorite",
                    modifier = Modifier
                        .height(60.dp)
                        .padding(dimensionResource(id = R.dimen.medium_padding)),
                        tint = colorResource(id = R.color.gold)
                )
                else{
                    Icon(
                        imageVector = Icons.Default.Star, contentDescription = "Add to Favorite",
                        modifier = Modifier
                            .height(60.dp)
                            .padding(dimensionResource(id = R.dimen.medium_padding)),
                    )
                }
            }
        }
    }
}
@Composable 
fun ViewFavorites(
    favorites: List<favorite>
){
    Column(
        modifier = Modifier
            .padding(2.dp)
    ) {
        Text(text = "Favorite Flights",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.small_padding),
                    end = dimensionResource(id = R.dimen.small_padding))
                )
    LazyColumn {
        items(favorites){favorite->
            FavoriteCard(favorite = favorite)
        }
    }
}
}

@Composable
fun FavoriteCard(
    favorite: favorite
){
    Log.d("favorite","${favorite.departure_code}")
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.small_padding))
    ){
        Column {
            Text(
                text = "Depart",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.minor_padding))
            )
            Text(

                text = favorite.departure_code,
                style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Destination",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.minor_padding))
            )
            Text(
                text = favorite.destination_code,
                style = MaterialTheme.typography.titleMedium)
        }
    }
}
@Preview
@Composable
fun ViewScreen(){
    FlightSearchAppTheme {
    }
}