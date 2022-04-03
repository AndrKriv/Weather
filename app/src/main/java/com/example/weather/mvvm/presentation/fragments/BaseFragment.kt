package com.example.weather.mvvm.presentation.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.mvvm.presentation.factory.ViewModelFactory
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes val layoutId: Int) : Fragment(layoutId) {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    checkPermissions()
                    startLocationUpdates()
                }
                !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showAlertMessageWhenDeniedSecondTime()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showAlertMessageWhenDeniedFirstTime()
                }
            }
        }

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

    override fun onStart() {
        super.onStart()
        locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    abstract fun onWeatherDataReceived(latitude: String, longitude: String)

    private fun checkPermissions() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(LocationRequest())
        val task = LocationServices.getSettingsClient(requireContext())
            .checkLocationSettings(builder.build())

        task.addOnCompleteListener { result ->
            try {
                result.getResult(ApiException::class.java)
            } catch (e: ResolvableApiException) {
                e.startResolutionForResult(
                    requireActivity(),
                    Activity.RESULT_CANCELED
                )
            }
        }
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