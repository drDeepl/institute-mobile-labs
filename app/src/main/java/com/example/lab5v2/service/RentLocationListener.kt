package com.example.lab5v2.service

import android.location.Location
import android.location.LocationListener
import com.example.lab5v2.service.interfaces.LocationListenerI

class RentLocationListener : LocationListener {
    private lateinit var locationListenerInterface: LocationListenerI

    override fun onLocationChanged(location: Location) {
        locationListenerInterface.onLocationChanged(location)
    }

    fun setLocationListenerI(locationListenerI: LocationListenerI){
        this.locationListenerInterface = locationListenerI
    }



}