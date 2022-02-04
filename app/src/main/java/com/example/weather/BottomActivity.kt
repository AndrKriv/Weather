package com.example.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather.connection.CheckConnection
import com.example.weather.databinding.ActivityBottomBinding
import com.example.weather.objects.ConstForAllProject
import com.google.android.gms.location.*
import java.util.*

class BottomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBottomBinding
    var string: String? = ""
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    val connection = CheckConnection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mLocationRequest = LocationRequest()
        val navBar = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_bottom)
        navBar.setupWithNavController(navController)
        if (connection.isInternetChecking(this)) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps()
            }
        } else Toast.makeText(this, R.string.connection, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        if (checkPermissionForLocation(this)) {
            startLocationUpdates()
        }
    }

    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ConstForAllProject.REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }

    protected fun startLocationUpdates() {
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        Looper.myLooper()?.let {
            mFusedLocationProviderClient?.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                it
            )
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        val sharedPreference =
            getSharedPreferences(ConstForAllProject.prefName, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString(ConstForAllProject.latitude, location.latitude.toString())
        editor.putString(ConstForAllProject.longitude, location.longitude.toString())
        editor.apply()
    }

    private fun stopLocationUpdates() {
        mFusedLocationProviderClient?.removeLocationUpdates(mLocationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == ConstForAllProject.REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {// доступ к gps разрешен, открываем локацию (пункт настроек)
                startLocationUpdates()
            } else {
                Toast.makeText(this@BottomActivity, R.string.denied, Toast.LENGTH_SHORT).show()
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.location)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { dialog, id ->
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), ConstForAllProject.requestCode
                )
            }
            .setNegativeButton(R.string.no) { dialog, id ->
                dialog.cancel()
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()//onstop or onpause
    }
}