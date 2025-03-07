package org.example.project

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AndroidMapController(private val context: Context, private val mapFragment: SupportMapFragment) :
    MapController, OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private var currentPolyline: Polyline? = null
    private lateinit var coordinates: List<Pair<Double, Double>>
    private val markerList = mutableListOf<Marker>()
    private lateinit var initialPosition: Position
    private lateinit var finalPosition: Position
    private var changeFinalPosition: Position? = null

    override fun initializeMap(initialPosition: Position, finalPosition: Position, changeFinalPosition: Position?) {
        this.initialPosition = initialPosition
        this.finalPosition = finalPosition
        this.changeFinalPosition = changeFinalPosition
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Enable built-in UI controls
        googleMap!!.uiSettings.apply {
            isZoomControlsEnabled = true    // Zoom buttons (plus/minus)
            isCompassEnabled = true           // Compass icon
            isMapToolbarEnabled = true        // Toolbar for directions, etc.
            isMyLocationButtonEnabled = true  // My Location button (if location permissions are granted)
        }

        // Set an initial camera position (this example zooms out to show a larger area)
        val initialLocation = LatLng(initialPosition.latitude, initialPosition.longitude) // Example: San Francisco
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10f))

        mapFragment.lifecycleScope.launch {
            try {
                subscribeToService(
                    "http://10.0.2.2:3000/route",
                    initialPosition,
                    finalPosition,
                    changeFinalPosition,
                    10_000L
                ).collect { response ->
                    if(!response.isAlert){
                        if(!response.isOriginalRoute){
                            showToast("Route has changed")
                        }
                        coordinates = buildCoordinateList(response)
                        drawRoute(coordinates)
                    }else{
                        showToast(response.message)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Optionally, handle the error or update the UI
            }
        }

    }

    override fun addMarker(latitude: Double, longitude: Double, title: String) {
        val marker = googleMap?.addMarker(MarkerOptions().position(LatLng(latitude, longitude)).title(title))
        marker?.let { markerList.add(it) }
    }

    override fun drawRoute(points: List<Pair<Double, Double>>) {
        googleMap?.let { map ->
            // Remove previously drawn route, if any
            currentPolyline?.remove()

            // Create a new polyline with the updated route
            val polylineOptions = PolylineOptions().apply {
                points.forEach { (lat, lng) ->
                    add(LatLng(lat, lng))
                }
                color(Color.BLUE)
                width(8f)
            }
            // Add the new polyline and keep its reference
            currentPolyline = map.addPolyline(polylineOptions)

            // Optionally, adjust the camera to fit the route
            val builder = LatLngBounds.Builder()
            points.forEach { (lat, lng) ->
                builder.include(LatLng(lat, lng))
            }
            val bounds = builder.build()
            val padding = 100 // offset in pixels
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        } ?: run {
            Log.e("AndroidMapController", "Google Map not ready; cannot draw route")
        }
    }
    private fun buildCoordinateList(routeResponse: RouteResponse): List<Pair<Double, Double>> {
        val coordinateList = mutableListOf<Pair<Double, Double>>()

        // Check initial position
        coordinateList.add(routeResponse.initialPosition.latitude to routeResponse.initialPosition.longitude)

        // Log the coordinates list size for debugging
        println("Motorcycle route has ${routeResponse.motorcycleRoute.coordinates.size} coordinate sets.")

        // Add each coordinate from motorcycleRoute.coordinates
        routeResponse.motorcycleRoute.coordinates.forEach { coordinate ->
            if (coordinate.size >= 2) {
                coordinateList.add(coordinate[1] to coordinate[0])
            } else {
                // Log a warning if the coordinate doesn't have two elements
                println("Warning: Expected at least 2 elements but got ${coordinate.size} in $coordinate")
            }
        }

        // Add final position
        coordinateList.add(routeResponse.finalPosition.latitude to routeResponse.finalPosition.longitude)

        return coordinateList
    }

    override fun searchQuery(query: String) {
        removeAllMarkers()
        val randomCoordinates = coordinates.shuffled().take(3)
        for (coordinate in randomCoordinates){
            addMarker(coordinate.first, coordinate.second, query)
        }
    }

    override fun removeAllMarkers() {
        for(marker in markerList){
            marker.remove()
        }
        markerList.clear()
    }
}
