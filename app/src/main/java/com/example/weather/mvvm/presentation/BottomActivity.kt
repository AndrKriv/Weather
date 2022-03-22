package com.example.weather.mvvm.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.weather.databinding.ActivityBottomBinding

class BottomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigation = binding.navView
        val navigationController =
            binding.navHostFragmentActivityBottom.getFragment<NavHostFragment>().navController
        navigation.setupWithNavController(navigationController)
    }
}