package org.example.project


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteResponse(
    val initialPosition: Position = Position(0.0, 0.0),  // Example default value
    val finalPosition: Position = Position(0.0, 0.0),
    val motorcycleRoute: MotorcycleRoute = MotorcycleRoute(emptyList()),
    var isOriginalRoute: Boolean = true,
    var isAlert: Boolean = false,
    var message: String = ""
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
