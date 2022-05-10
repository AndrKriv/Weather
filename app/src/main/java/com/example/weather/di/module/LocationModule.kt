package com.example.weather.di.module

import android.content.Context
import com.example.weather.core.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocationModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideLocationRequest(): LocationRequest = LocationRequest.create()

    @Provides
    @Singleton
    fun provideLocationSettingsRequest(locationRequest: LocationRequest): LocationSettingsRequest =
        LocationSettingsRequest
            .Builder()
            .addLocationRequest(locationRequest)
            .build()
}