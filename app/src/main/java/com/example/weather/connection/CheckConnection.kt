package com.example.weather.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

open class CheckConnection {
     fun checkForInternet(context: Context): Boolean {
        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}
//fun isOnline(context: Context): Boolean {
//    val connectivityManager =
//        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    if (connectivityManager != null) {
//        val capabilities =
//            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
//        if (capabilities != null) {
//            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
//                return true
//            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
//                return true
//            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
//                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
//                return true
//            }
//        }
//    }
//    return false
//}
