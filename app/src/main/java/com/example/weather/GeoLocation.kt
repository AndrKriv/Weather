package com.example.weather

import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat.getSystemService

class GeoLocation {
//    private var locationManager : LocationManager? = null
//
//fun x(){
//        // Create persistent LocationManager reference
//        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
//
//            try {
//                // Request location updates
//                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
//            } catch(ex: SecurityException) {
//                Log.d("myTag", "Security Exception, no location available")
//            }
//
//    }
//
//    //define the listener
//    private val locationListener: LocationListener = object : LocationListener {
//        override fun onLocationChanged(location: Location) {
//            thetext.text = ("" + location.longitude + ":" + location.latitude)
//        }
//        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
//        override fun onProviderEnabled(provider: String) {}
//        override fun onProviderDisabled(provider: String) {}
//}
}