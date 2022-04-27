package com.example.weather.mvvm.presentation.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.mvvm.domain.location.LocationManager
import com.example.weather.mvvm.presentation.factory.ViewModelFactory
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes val layoutId: Int) : Fragment(layoutId) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var locationSettingsRequest: LocationSettingsRequest

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

    private val resolutionForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) startLocationUpdates()
            else requestGps()
        }

    override fun onStart() {
        super.onStart()
        retrieveData()
    }

    abstract fun onLocationReceived(latitude: String, longitude: String)

    fun retrieveData() = locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)

    private fun startLocationUpdates() =
        locationManager.requestLocationUpdates {
            onLocationReceived(it.latitude.toString(), it.longitude.toString())
        }

    private fun requestGps() {
        LocationServices
            .getSettingsClient(requireContext())
            .checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener { startLocationUpdates() }
            .addOnFailureListener(::checkGPSException)
    }

    private fun checkGPSException(exception: Exception) =
        if (exception is ResolvableApiException) resolveApiException(exception)
        else Toast.makeText(requireContext(), exception.message.toString(), Toast.LENGTH_SHORT).show()

    private fun resolveApiException(exception: ResolvableApiException) =
        try {
            resolutionForResult.launch(
                IntentSenderRequest.Builder(exception.resolution).build()
            )
        } catch (throwable: Throwable) {
            Toast.makeText(requireContext(), throwable.message.toString(), Toast.LENGTH_SHORT).show()
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