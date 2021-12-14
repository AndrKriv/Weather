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
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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

class BottomActivity : AppCompatActivity() {
    var string:String?=""
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    //private val INTERVAL: Long = 50000
    //private val FASTEST_INTERVAL: Long = 50000
    //lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 1000

        //private lateinit var binding: ActivityBottomBinding
    val connection = CheckConnection()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bottom)
        //binding = ActivityBottomBinding.inflate(layoutInflater)
        //setContentView(binding.root)
        val navBar = findViewById<BottomNavigationView>(R.id.nav_view)
        mLocationRequest = LocationRequest()

        loadFragment(TodayFragment().newInstance())

        navBar.setOnClickListener { item ->
            var fragment: Fragment
            when (item.id) {
                R.id.tf -> {
                    //toolbar?.setTitle("Home")
                    fragment = TodayFragment()
                    loadFragment(fragment)
                    true
                }
                R.id.ff -> {
                    //toolbar?.setTitle("Radio")
                    fragment = ForecastFragment()
                    loadFragment(fragment)
                    true

                }
                else->false
            }
        }
//        if(connection.checkForInternet(this)) {
//
//            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                buildAlertMessageNoGps()
//            }

//            val navController = findNavController(R.id.nav_host_fragment_activity_bottom)
//
//            val navView: BottomNavigationView = binding.navView
//            navView.setupWithNavController(navController)
       // }
        //else Toast.makeText(this,"Проверьте Ваше интернет-соединение!",Toast.LENGTH_SHORT).show()
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_bottom, fragment)
            .commit()
    }


    override fun onStart() {
        super.onStart()
        if (checkPermissionForLocation(this)) {
            startLocationUpdates()
            Log.d("onStart","start using location")
        }
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
            // do work here
            //locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        // New location has now been determined
        //mLastLocation = location
        val date: Date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("hh:mm:ss a")
        Log.d("TXT1",sdf.format(date))
        Log.d("TXT2",location.latitude.toString())
        Log.d("TXT3",location.longitude.toString())


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val myFragment = TodayFragment()
        val bundle = Bundle()
        bundle.putString("lat", location.latitude.toString())
        bundle.putString("lon", location.longitude.toString())
        myFragment.arguments = bundle
        fragmentTransaction.add(R.id.tf, myFragment).commit()
        // You can now create a LatLng Object for use with maps
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