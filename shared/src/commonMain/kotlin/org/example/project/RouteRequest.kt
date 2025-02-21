package org.example.project

import kotlinx.serialization.Serializable

@Serializable
data class RouteRequest(
    val initialPosition: Position,
    val finalPosition: Position
)