package com.example.weather.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.weather.core.connection.NetworkStateManager
import com.example.weather.databinding.ActivityBottomBinding
import javax.inject.Inject

class BottomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomBinding

    @Inject
    lateinit var networkStateManager: NetworkStateManager

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

        lifecycleScope.launchWhenStarted {
            networkStateManager.state.collect {
                binding.errorMessage.isVisible = !it
            }
        }
    }
}