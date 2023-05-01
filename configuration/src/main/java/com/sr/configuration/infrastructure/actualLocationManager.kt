package com.sr.configuration.infrastructure

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult


object actualLocationManager {

    var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun getActualLocation(fusedLocationClient: FusedLocationProviderClient, function: (Pair<Double, Double>) -> Unit) {
       locationCallback  = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    // Actualiza la ubicación
                    val latitude = location.latitude
                    val longitude = location.longitude
                    function.invoke(Pair(latitude, longitude))
                    println("ubicación : ")
                    // Haz lo que quieras con la ubicación actualizada
                }
            }
        }

        // Registra el LocationCallback
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // Intervalo de actualización en milisegundos
            fastestInterval = 5000 // Intervalo más rápido de actualización en milisegundos
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback?.let {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback as LocationCallback, Looper.getMainLooper())
        }


    }

    fun stopLocation(fusedLocationClient: FusedLocationProviderClient) {
        // Para dejar de recibir actualizaciones de ubicación
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
        locationCallback = null
    }

}