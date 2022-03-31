package com.example.weather.mvvm.presentation

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.weather.databinding.ActivityBottomBinding
import com.example.weather.mvvm.domain.usecase.InternetConnectionReader
import com.example.weather.mvvm.presentation.app.App
import javax.inject.Inject

class BottomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomBinding
    private lateinit var connectionLiveData: InternetConnectionReader

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navigation = binding.navView
        val navigationController =
            binding.navHostFragmentActivityBottom.getFragment<NavHostFragment>().navController
        navigation.setupWithNavController(navigationController)

        (applicationContext as App)
            .appComponent
            .inject(this)
        connectionLiveData = InternetConnectionReader(this, connectivityManager)
        connectionLiveData.checkNetworkState()
        connectionLiveData.getLiveData().observe(this) {
            if (it) {
                binding.errorMessage.visibility = View.GONE
            } else {
                binding.errorMessage.visibility = View.VISIBLE
            }
        }
    }
}