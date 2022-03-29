package com.example.weather.mvvm.presentation.fragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.weather.mvvm.presentation.factory.ViewModelFactory
import com.example.weather.utils.Constants
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes val layoutId: Int) : Fragment(layoutId) {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    @Inject
    lateinit var viewModelsFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (loc in locationResult.locations) {
                    onWeatherDataReceived(loc.latitude.toString(), loc.longitude.toString())
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requestPermissionsIfNeeded()) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    abstract fun onWeatherDataReceived(latitude: String, longitude: String)

    private fun requestPermissionsIfNeeded(): Boolean {
        return if (checkPermissions()) {
            return true
        } else {
            showPermissionWindowToUser(requireActivity())
            false
        }
    }

    private fun checkPermissions(): Boolean {
        if (
            checkSelfPermissions(Manifest.permission.ACCESS_COARSE_LOCATION) ||
            checkSelfPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(LocationRequest())
            val task = LocationServices.getSettingsClient(requireContext())
                .checkLocationSettings(builder.build())
            task.addOnCompleteListener { result ->
                try {
                    result.getResult(ApiException::class.java)
                } catch (e: ResolvableApiException) {
                    e.startResolutionForResult(requireActivity(), Constants.REQUEST_CODE)
                }
            }
            return true
        }
        return false
    }

    private fun checkSelfPermissions(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun showPermissionWindowToUser(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            Constants.REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(Constants.REQUEST_CODE, permissions, grantResults)
    }

    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest(),
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}