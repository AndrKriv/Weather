package com.example.weather.connection

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.example.weather.R
import com.example.weather.objects.Constants

object AlertDialogs {
    private fun wifiOpener(context:Context){//wifi
        context.startActivity(
            Intent(Settings.ACTION_WIFI_SETTINGS)
        )
    }
    private fun locationOpener(context:Context){//локация
        context.startActivity(
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        )
    }
    fun operatorOpener(context:Context){//мобильные сети
        context.startActivity(
            Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
        )
    }
    fun wirelessOpener(context:Context){//подключения
        context.startActivity(
            Intent(Settings.ACTION_WIRELESS_SETTINGS)
        )
    }

    fun Activity.buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.location)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { _, _ ->
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Constants.requestCode
                )
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun buildAlertMessageNoGps(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.location)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { _, _ ->
                locationOpener(context)
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun buildAlertMessageNoWifi(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.internet)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { _, _ ->
                wifiOpener(context)
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun Activity.buildAlertMessageNoWifi() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.internet)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { _, _ ->
                wifiOpener(this)
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}