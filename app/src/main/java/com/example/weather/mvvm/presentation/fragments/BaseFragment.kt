package com.example.weather.mvvm.presentation.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.weather.objects.Constants
import com.google.android.gms.location.*

abstract class BaseFragment : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var locationCallback: LocationCallback

    override fun onResume() {
        super.onResume()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (loc in locationResult.locations) {
                    onReadWeather(loc.latitude.toString(), loc.longitude.toString())
                }
            }
        }
        loadAllChecks()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    abstract fun onReadWeather(latitude: String, longitude: String)

    private fun loadAllChecks() {
        if (checkPermissions(requireContext())) {
            if (isLocationEnabled(requireContext())) {
                startLocationUpdates()
            } else {
                buildAlertMessageNoGps(requireContext())
            }
        } else {
            loadPermissionWindow(requireActivity())
        }
    }

    private fun checkPermissions(context: Context): Boolean {//granted permission or not
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun loadPermissionWindow(activity: Activity) { //Функция запроса разрешения у пользователя
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            Constants.REQUEST_CODE
        )
    }

    private fun isLocationEnabled(context: Context): Boolean { //если локация включена
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun buildAlertMessageNoGps(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage("Локация выключена. Включить?")
            .setCancelable(false)
            .setPositiveButton("Да") { _, _ ->
                context.startActivity(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            }
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.cancel()
                buildAlertMessageNoGps(context)
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