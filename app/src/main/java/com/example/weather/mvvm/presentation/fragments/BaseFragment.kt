package com.example.weather.mvvm.presentation.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.objects.Constants
import com.google.android.gms.location.*

abstract class BaseFragment : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return getInflater(inflater, container)
    }

    private fun getInflater(inflater: LayoutInflater, container: ViewGroup?): View {
        return if (ForecastFragment().javaClass.name == javaClass.name)
            inflater.inflate(R.layout.fragment_forecast, container, false)
        else inflater.inflate(R.layout.fragment_today, container, false)
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
        return if (checkPermissions(requireContext())) {
            if (isLocationEnabled(requireContext())) {
                true
            } else {
                showAlertMessageNoGps(requireContext())
                false
            }
        } else {
            showPermissionWindowToUser(requireActivity())
            false
        }
    }

    private fun checkPermissions(
        context: Context
    ): Boolean {
        if (
            checkSelfPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, context) ||
            checkSelfPermissions(Manifest.permission.ACCESS_FINE_LOCATION, context)
        ) {
            return true
        }
        return false
    }

    private fun checkSelfPermissions(permissions: String, context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            permissions
        ) == PackageManager.PERMISSION_GRANTED
    }

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

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showAlertMessageNoGps(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(getString(R.string.location))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                context.startActivity(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
                showAlertMessageNoGps(context)
            }
        val alert: AlertDialog = builder.create()
        alert.show()
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