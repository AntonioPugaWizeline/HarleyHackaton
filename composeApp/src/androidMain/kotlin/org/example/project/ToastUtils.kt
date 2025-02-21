package org.example.project

import android.content.Context
import android.widget.Toast

lateinit var appContext: Context

fun showToast(message: String) {
    Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
}
