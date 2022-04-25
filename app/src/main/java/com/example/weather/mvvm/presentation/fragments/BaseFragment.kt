package com.example.weather.mvvm.presentation.fragments

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.mvvm.domain.location.LocationManager
import com.example.weather.mvvm.presentation.factory.ViewModelFactory
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes val layoutId: Int) : Fragment(layoutId) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val locationManager by lazy { LocationManager(requireContext()) }

    private val locationRequest = LocationRequest.create()

    private val locationSettingsRequest = LocationSettingsRequest
        .Builder()
        .addLocationRequest(locationRequest)
        .build()

    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    requestGps()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showAlertMessageWhenDeniedFirstTime()
                }
                !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showAlertMessageWhenDeniedSecondTime()
                }
            }
        }

    override fun onStart() {
        super.onStart()
        locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onStop() {
        super.onStop()
        locationManager.removeLocation()
    }

    abstract fun onLocationReceived(latitude: String, longitude: String): Unit?

    abstract fun showProgressBar()
    abstract fun hideProgressBar()

    private fun setLocationListener() =
        locationManager.requestLocationUpdates {
            onLocationReceived(it.latitude.toString(), it.longitude.toString())
        }

    private fun requestGps() {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        LocationServices
            .getSettingsClient(requireContext())
            .checkLocationSettings(
                locationSettingsRequest
            )
            .addOnCompleteListener {
                setLocationListener()
            }
            .addOnFailureListener { exception ->
                gpsIsDisabled(exception)
            }
    }

    private fun gpsIsDisabled(exception: Exception) {
        if (exception is ResolvableApiException) {
            try {
                exception.startResolutionForResult(
                    requireActivity(),
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED
                )
            } catch (sendEx: IntentSender.SendIntentException) {
            }
        }
    }

    private fun showAlertMessageWhenDeniedSecondTime() =
        AlertDialog
            .Builder(requireContext())
            .setMessage(getString(R.string.second_dialog_message))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + requireActivity().packageName)
                    )
                )
            }
            .setNegativeButton(getString(R.string.close)) { _, _ ->
                Toast.makeText(
                    requireContext(),
                    getString(R.string.old_location_data),
                    Toast.LENGTH_LONG
                ).show()
            }
            .create()
            .show()

    private fun showAlertMessageWhenDeniedFirstTime() {
        AlertDialog
            .Builder(requireContext())
            .setMessage(getString(R.string.first_dialog_message))
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { _, _ ->
                locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .create()
            .show()
    }
}