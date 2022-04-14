package com.example.weather.mvvm.presentation.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.mvvm.domain.location.LocationManager
import com.example.weather.mvvm.presentation.factory.ViewModelFactory
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes val layoutId: Int) : Fragment(layoutId) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            for (loc in locationResult.locations) {
                onWeatherDataReceived(loc.latitude.toString(), loc.longitude.toString())
            }
        }
    }
    private val locationManager by lazy { LocationManager(requireContext(), locationCallback) }

    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    requestGps()
                }
                !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showAlertMessageWhenDeniedSecondTime()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showAlertMessageWhenDeniedFirstTime()
                }
            }
        }

    override fun onStart() {
        super.onStart()
        locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onStop() {
        super.onStop()
        locationManager.removeUpdates()
    }

    abstract fun onWeatherDataReceived(latitude: String, longitude: String)

    private fun requestGps() {
        val task =
            LocationServices
                .getSettingsClient(requireContext())
                .checkLocationSettings(
                    LocationSettingsRequest
                        .Builder()
                        .addLocationRequest(LocationRequest.create())
                        .build()
                )
        task.addOnCompleteListener {
            locationManager.requestUpdates()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        requireActivity(),
                        Activity.RESULT_CANCELED
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                }
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