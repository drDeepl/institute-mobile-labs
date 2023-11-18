package com.example.lab5v2.service.interfaces

import android.location.Location

interface LocationListenerI {

    fun onLocationChanged(location: Location)

}