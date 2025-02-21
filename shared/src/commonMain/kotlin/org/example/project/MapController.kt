package org.example.project

interface MapController {
    fun initializeMap()
    fun addMarker(latitude: Double, longitude: Double, title: String)
    fun drawRoute(points: List<Pair<Double, Double>>)
}