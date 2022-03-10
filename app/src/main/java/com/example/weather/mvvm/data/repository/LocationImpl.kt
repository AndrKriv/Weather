package com.example.weather.mvvm.data.repository

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weather.mvvm.domain.models.Location
import com.example.weather.mvvm.domain.repository.LocationInterface
import com.example.weather.objects.Constants
import com.google.android.gms.location.*

class LocationImpl(private val context: Context) : LocationInterface {
    override fun getLocation(): Location {
        return Location(
            50.0,60.0
        )
    }

//    var string: String? = ""
//    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
//    lateinit var mLocationRequest: LocationRequest
//
//    fun st() {
//        mLocationRequest = LocationRequest()
//
//        val locationManager =
//            context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
////        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
////            buildAlertMessageNoGps()
////        }
//
//
//        if (checkPermissions()) {
//            startLocationUpdates()
//        }
//    }
//
////    private fun checkPermissionForLocation(context: Context): Boolean {
////        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
////                PackageManager.PERMISSION_GRANTED
////            ) {
////                true
////            } else {
////                ActivityCompat.requestPermissions(
////                    this,
////                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
////                    Constants.REQUEST_PERMISSION_LOCATION
////                )
////                false
////            }
////        } else {
////            true
////        }
////    }
//
//    private fun checkPermissions(): Boolean {
//        if (
//            ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
//                (
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            return true
//        }
//        return false
//    }
//
//
//    protected fun startLocationUpdates() {
//        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        val builder = LocationSettingsRequest.Builder()
//        builder.addLocationRequest(mLocationRequest)
//        val locationSettingsRequest = builder.build()
//        val settingsClient = LocationServices.getSettingsClient(context)
//        settingsClient.checkLocationSettings(locationSettingsRequest)
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
//            PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) !=
//            PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//
//        Looper.myLooper()?.let {
//            mFusedLocationProviderClient?.requestLocationUpdates(
//                mLocationRequest, mLocationCallback,
//                it
//            )
//        }
//    }
//
//    private val mLocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            onLocationChanged(locationResult.lastLocation)
//        }
//    }
//
//    fun onLocationChanged(location: android.location.Location) {
//        val sharedPreference =
//            context.getSharedPreferences(Constants.preferencesName, Context.MODE_PRIVATE)
//        val editor = sharedPreference.edit()
//        editor.putString(Constants.latitude, location.latitude.toString())
//        editor.putString(Constants.longitude, location.longitude.toString())
//        editor.apply()
//    }
//
////    override fun getLocation(): Location {
////        return Location(
////            latitude = SharedPreference(context = context).getValueString(Constants.latitude)
////                .toString().toDouble(),
////            longitude = SharedPreference(context = context).getValueString(Constants.longitude)
////                .toString().toDouble()
////        )
////    }
//
//
//
//    class SharedPreference(val context: Context) {
//        private val PREFS_NAME = Constants.preferencesName
//        val sharedPref: SharedPreferences =
//            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//
//        fun getValueString(KEY_NAME: String): String? {
//            return sharedPref.getString(KEY_NAME, null)
//        }
//    }
//
//    private fun stopLocationUpdates() {
//        mFusedLocationProviderClient?.removeLocationUpdates(mLocationCallback)
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == Constants.REQUEST_PERMISSION_LOCATION) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {// доступ к gps разрешен, открываем локацию (пункт настроек)
//                startLocationUpdates()
//            } else {
//                Toast.makeText(this@BottomActivity, R.string.denied, Toast.LENGTH_SHORT).show()
//            }
//        } else
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == Constants.REQUEST_PERMISSION_LOCATION) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                startLocationUpdates()
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        }
//    }
//
//    private fun buildAlertMessageNoGps() {
//        val builder = AlertDialog.Builder(context)
//        builder.setMessage(R.string.location)
//            .setCancelable(false)
//            .setPositiveButton(R.string.yes) { _, _ ->
//                startActivityForResult(
//                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Constants.requestCode
//                )
//            }
//            .setNegativeButton(R.string.no) { dialog, _ ->
//                dialog.cancel()
//                finish()
//            }
//        val alert: AlertDialog = builder.create()
//        alert.show()
//    }


    //stopLocationUpdates()//onstop or onpause

}