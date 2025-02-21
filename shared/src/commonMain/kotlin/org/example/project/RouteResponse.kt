package org.example.project


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteResponse(
    val initialPosition: Position,
    val finalPosition: Position,
    val motorcycleRoute: MotorcycleRoute,
    var isOriginalRoute: Boolean = true
)

@Serializable
data class Position(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class MotorcycleRoute(
    val coordinates: List<List<Double>>
)
