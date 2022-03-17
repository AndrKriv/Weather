package com.example.weather.mvvm.presentation.fragments

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.weather.mvvm.core.ForecastInfo
import com.google.android.gms.location.FusedLocationProviderClient

class BaseFragment(private val activity: Activity) : Fragment() {

    private val lat = ""
    private val lon = ""

    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLocationPermissionGranted(context = requireContext(), activity)

    }

    private fun isLocationPermissionGranted(context: Context, activity: Activity): Boolean {

        return if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                42
                //requestcode
            )
            false
        } else {
            true
        }
    }


}