package com.example.weather.mvvm.domain.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class LocationManager(val context: Context) {

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val locationCallback: LocationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationUpdatesCompleteAction.invoke(locationResult.lastLocation)
            }
        }

    private val locationRequest: LocationRequest = LocationRequest.create()

    private lateinit var locationUpdatesCompleteAction: ((Location) -> Unit)

    fun requestLocationUpdates(locationListener: (Location) -> Unit) {
        this.locationUpdatesCompleteAction = locationListener
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

    }

    fun removeLocation() = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
}