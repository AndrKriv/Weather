package com.example.weather.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.weather.core.connection.NetworkStateManager
import com.example.weather.databinding.ActivityBottomBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class BottomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomBinding

    @Inject
    lateinit var networkStateManager: NetworkStateManager

    private lateinit var disposable: Disposable

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

        disposable = networkStateManager
            .connectionObserver
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.errorMessage.isVisible = !it
            }, {
                Log.e("AAA", it.message.toString())
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}