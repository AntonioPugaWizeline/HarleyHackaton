package org.example.project

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.project.MapFragmentScreen

@Composable
fun MainScreen() {
    var selectedIndex by remember { mutableStateOf(0) }
    val items = listOf("Home", "Connect", "Route", "More")
    val viewModel: SearchViewModel = SearchViewModel()

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = Color.Black
            ) {

                items.forEachIndexed { index, item ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = when (item) {
                                    "Home" -> Icons.Default.Home
                                    "Connect" -> Icons.Default.Groups
                                    "Route" -> Icons.Default.Map
                                    "More" -> Icons.Default.MoreHoriz
                                    else -> Icons.Default.Home
                                },
                                contentDescription = item
                            )
                        },
                        label = { Text(item) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        selectedContentColor = Color.Red,
                        unselectedContentColor = Color.LightGray
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedIndex) {
                0 -> HomeScreen()
                1 -> Text("Connect Screen")
                2 -> MapFragmentScreen( viewModel = viewModel)
                3 -> BikeStatusScreen()
            }
        }
    }
}
