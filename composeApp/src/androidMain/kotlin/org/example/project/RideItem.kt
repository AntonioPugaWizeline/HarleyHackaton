package org.example.project

import androidx.compose.ui.graphics.vector.ImageVector

data class RideItem (
    val icon: ImageVector,
    val title: String,
    val description: String,
    val initialPosition: Position,
    val finalPosition: Position,
    val changeFinalPosition: Position? = null
)