package com.example.project

import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterHdr
import androidx.compose.material.icons.filled.Surfing
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.SupportMapFragment
import org.example.project.*

@Composable
fun MapFragmentScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = SearchViewModel()
) {
    var selectedCard by remember { mutableStateOf<RideItem?>(null) }

    if(selectedCard == null){
        val rideItems = listOf(
            RideItem(icon = Icons.Filled.FilterHdr, title = "Yosemite National Parl", description = "Camping route, climbing and mountain adventures.", Position(40.730610, -75.935242), Position(40.741895, -73.989308)),
            RideItem(icon = Icons.Filled.Surfing, title = "San Diego, County", description = "Surfing route from beach of florida to san diego.", Position(38.261520, -119.2639655), Position(37.859933, -119.5383381)),
        )
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                Text(
                    text = "Last rides",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Divider(modifier = Modifier.padding(bottom = 8.dp))
            }
            items(rideItems) { item ->
                RideItemView(
                    icon = item.icon,
                    title = item.title,
                    description = item.description,
                    onClick = { selectedCard = item }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
            item {
                Text(
                    text = "Popular rides",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Divider(modifier = Modifier.padding(bottom = 8.dp))
            }
            items(rideItems) { item ->
                RideItemView(
                    icon = item.icon,
                    title = item.title,
                    description = item.description,
                    onClick = { selectedCard = item }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }else{
        val query by viewModel.searchQuery.collectAsState()
        val activity = LocalContext.current as? AppCompatActivity
        val fragmentManager = activity?.supportFragmentManager

        // Use remember to store the MapController and allow recompositions to update it
        val mapControllerState = remember { mutableStateOf<AndroidMapController?>(null) }

        Box(modifier = modifier.fillMaxSize()) {
            // Google Maps Fragment
            AndroidView(
                factory = { context ->
                    FrameLayout(context).apply {
                        id = View.generateViewId()

                        if (fragmentManager != null) {
                            post {
                                val mapFragment = SupportMapFragment.newInstance()
                                fragmentManager.beginTransaction()
                                    .replace(this.id, mapFragment)
                                    .commitNow()

                                val controller = AndroidMapController(context, mapFragment)
                                controller.initializeMap(selectedCard!!.initialPosition, selectedCard!!.finalPosition, selectedCard!!.changeFinalPosition)
                                mapControllerState.value = controller  // Update state with controller
                            }
                        }
                    }
                },
                modifier = Modifier.matchParentSize()
            )

            // Search bar on top of the map
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            ) {
                SearchBar(
                    query = query,
                    onQueryChanged = viewModel::updateSearchQuery,
                    onSearchClicked = {
                        mapControllerState.value?.searchQuery(viewModel.getSearchQuery())
                    }
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Space between FABs
            ) {
                FloatingActionButton(
                    onClick = { selectedCard = null },
                    backgroundColor = Color.Red
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Add Marker", tint = Color.White)
                }
            }
        }
    }


}


