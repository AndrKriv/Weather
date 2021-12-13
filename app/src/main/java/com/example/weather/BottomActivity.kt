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
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather.connection.CheckConnection
import com.example.weather.databinding.ActivityBottomBinding
import com.example.weather.share.ShareText
import com.example.weather.ui.today.TodayFragment
import com.google.android.gms.location.*
import java.text.SimpleDateFormat
import java.util.*

class BottomActivity : AppCompatActivity() {
    var string:String?=""
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10

    private lateinit var binding: ActivityBottomBinding
    val connection = CheckConnection()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mLocationRequest = LocationRequest()

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }

        if(connection.checkForInternet(this)) {
            val navController = findNavController(R.id.nav_host_fragment_activity_bottom)

            val navView: BottomNavigationView = binding.navView
            navView.setupWithNavController(navController)
        }
        else Toast.makeText(this,"Проверьте Ваше интернет-соединение!",Toast.LENGTH_SHORT).show()

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
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    , 11)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()

    }


    protected fun startLocationUpdates() {
        // Create the location request to start receiving updates
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.setInterval(INTERVAL)
        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return
        }
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        // New location has now been determined



        mLastLocation = location
        val date: Date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("hh:mm:ss a")
        //string = mLastLocation.latitude.toString()
        Log.d("TXT1",sdf.format(date))
        Log.d("TXT2",mLastLocation.latitude.toString())
        Log.d("TXT3",mLastLocation.longitude.toString())


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val myFragment = TodayFragment()
        val bundle = Bundle()
        bundle.putString("message", mLastLocation.latitude.toString())
        myFragment.arguments = bundle
        fragmentTransaction.add(R.id.tf, myFragment).commit()
        // You can now create a LatLng Object for use with maps
    }

    private fun stoplocationUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)

        Log.d("Crash","Delete upgrading location")
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
               // btnStartupdate.isEnabled = false
                //btnStopUpdates.isEnabled = true
            } else {
                Toast.makeText(this@BottomActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
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


    override fun onStop() {
        super.onStop()
        stoplocationUpdates()
        Log.d("onStop","stop using location")
    }
    override fun onDestroy() {
        super.onDestroy()
        stoplocationUpdates()
        Log.d("onDestroy","destoy using location")
    }
}