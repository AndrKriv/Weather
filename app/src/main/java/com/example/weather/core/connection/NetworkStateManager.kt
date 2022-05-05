package com.example.weather.core.connection

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NetworkStateManager @Inject constructor() {

    private val _state = MutableStateFlow<Boolean>(true)
    val state = _state.asStateFlow()

    fun setNetworkConnectivityStatus(connectivityStatus: Boolean) {
        _state.value = connectivityStatus
    }
}