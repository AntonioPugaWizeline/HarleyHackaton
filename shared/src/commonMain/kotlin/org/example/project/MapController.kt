package org.example.project

interface MapController {
    fun initializeMap(initialPosition: Position, finalPosition: Position, changeFinalPosition: Position? = null)
    fun addMarker(latitude: Double, longitude: Double, title: String)
    fun drawRoute(points: List<Pair<Double, Double>>)
    fun searchQuery(query: String)
    fun removeAllMarkers()
}