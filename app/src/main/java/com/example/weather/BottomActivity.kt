package com.example.weather

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather.connection.CheckConnection
import com.example.weather.databinding.ActivityBottomBinding

class BottomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomBinding
    val connection = CheckConnection()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(connection.checkForInternet(this)) {
            val navController = findNavController(R.id.nav_host_fragment_activity_bottom)

            val navView: BottomNavigationView = binding.navView
            navView.setupWithNavController(navController)
        }
        else Toast.makeText(this,"Проверьте Ваше интернет-соединение!",Toast.LENGTH_SHORT).show()

    }
}