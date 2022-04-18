package com.example.weather.mvvm.domain.connection

import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class NetworkStateManager @Inject constructor() {

    val behaviorSubject = BehaviorSubject.create<Boolean>()

    fun setNetworkConnectivityStatus(connectivityStatus: Boolean) {
        behaviorSubject.onNext(connectivityStatus)
    }
}