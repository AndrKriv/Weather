package com.example.weather.mvvm.domain.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import javax.inject.Inject

class LocationManager @Inject constructor(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationRequest: LocationRequest
) {
    private lateinit var locationUpdatesCompleteAction: ((Location) -> Unit)

    private val locationCallback: LocationCallback =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationUpdatesCompleteAction.invoke(locationResult.lastLocation)
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }

    fun requestLocationUpdates(locationListener: (Location) -> Unit) {
        this.locationUpdatesCompleteAction = locationListener
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun removeLocationUpdates() = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
}