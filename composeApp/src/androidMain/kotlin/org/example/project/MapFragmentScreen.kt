package com.example.project

import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.SupportMapFragment
import org.example.project.AndroidMapController

@Composable
fun MapFragmentScreen(
    modifier: Modifier = Modifier
) {

    val activity = LocalContext.current as? AppCompatActivity
    val fragmentManager = activity?.supportFragmentManager


    AndroidView(
        factory = { context ->
            FrameLayout(context).apply {
                id = View.generateViewId()

                if(fragmentManager != null){
                    post {
                        val mapFragment = SupportMapFragment.newInstance()
                        fragmentManager.beginTransaction()
                            .replace(this.id, mapFragment)
                            .commitNow()

                        val mapController = AndroidMapController(context, mapFragment)
                        mapController.initializeMap()

                    }
                }

            }
        },
        modifier = modifier.fillMaxSize()
    )
}
