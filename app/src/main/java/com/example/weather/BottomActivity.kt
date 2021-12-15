package com.example.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.provider.Settings.System.putString
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather.connection.CheckConnection
import com.example.weather.databinding.ActivityBottomBinding
import com.example.weather.ui.forecast.ForecastFragment
import com.example.weather.ui.today.TodayFragment
import com.google.android.gms.location.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BottomActivity : AppCompatActivity(){
    var string:String?=""
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 1000
    val connection = CheckConnection()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bottom)

        val navBar = findViewById<BottomNavigationView>(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_activity_bottom)
        navBar.setupWithNavController(navController)
        mLocationRequest = LocationRequest()


        if(connection.checkForInternet(this)) {

//        navBar.setOnClickListener { item ->
//            var fragment: Fragment
//            when (item.id) {
//                R.id.tf -> {
//                    fragment = TodayFragment()
//                    loadFragment(fragment)
//                    true
//
//                }
//                R.id.ff -> {
//                    fragment = ForecastFragment()
//                    loadFragment(fragment)
//                    true
//
//                }
//
//            }
//        }

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps()
            }
            startLocationUpdates()

        }
        else Toast.makeText(this,"Проверьте Ваше интернет-соединение!",Toast.LENGTH_SHORT).show()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_bottom, fragment)
            .commit()
    }


    override fun onStart() {
        super.onStart()
        if (checkPermissionForLocation(this)) {

            Log.d("onStart","start using location")
        }
    }

    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // Show the permission request
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    protected fun startLocationUpdates() {
        // Create the location request to start receiving updates
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        // mLocationRequest.setInterval(INTERVAL)
        //  mLocationRequest.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            return
        }
        Looper.myLooper()?.let {
            mFusedLocationProviderClient?.requestLocationUpdates(mLocationRequest, mLocationCallback,
                it
            )
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            //locationResult.lastLocation
            // do work here
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location){

        // New location has now been determined
        //mLastLocation = location
        val date: Date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("hh:mm:ss a")
        Log.d("TXT1",sdf.format(date))
        Log.d("TXT2",location.latitude.toString())
        Log.d("TXT3",location.longitude.toString())
        // You can now create a LatLng Object for use with maps
        val sharedPreference =  getSharedPreferences("pref",Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("lat",location.latitude.toString())
        editor.putString("lon",location.longitude.toString())
        editor.apply()
        //loadFragment(TodayFragment.newInstance(location.latitude.toString(),location.longitude.toString()))
       // editor.commit()
        val sharedPref1 = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref1.edit()) {
            putString("key", location.latitude.toString())
            apply()
        }


//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        val myFragment = TodayFragment()
//        //bundle?.putString("lon", location.longitude.toString())
//        myFragment.arguments = bundleOf("lat" to location.latitude.toString(),"lon" to location.longitude.toString())
//        fragmentTransaction.add(R.id.tf, myFragment).commit()
    }
    private fun stoplocationUpdates() {
        mFusedLocationProviderClient?.removeLocationUpdates(mLocationCallback)
        Log.d("Crash","Delete upgrading location")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // доступ к gps разрешен, открываем локацию (пункт настроек)
                startLocationUpdates()
            } else {
                Toast.makeText(this@BottomActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Ваш GPS выключен. Включить?")
            .setCancelable(false)
            .setPositiveButton("Да") { dialog, id ->
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    , 11)
            }
            .setNegativeButton("Нет") { dialog, id ->
                dialog.cancel()
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onStop() {
        super.onStop()
        stoplocationUpdates()
        Log.d("onStop","stop using location")
    }
    override fun onDestroy() {
        super.onDestroy()
        stoplocationUpdates()
        Log.d("onDestroy","destroy using location")
    }
}