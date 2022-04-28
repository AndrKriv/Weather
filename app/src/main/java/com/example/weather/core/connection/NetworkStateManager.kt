package com.example.weather.core.connection

import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class NetworkStateManager @Inject constructor() {

    val connectionObserver = BehaviorSubject.create<Boolean>()

    fun setNetworkConnectivityStatus(connectivityStatus: Boolean) {
        connectionObserver.onNext(connectivityStatus)
    }
}