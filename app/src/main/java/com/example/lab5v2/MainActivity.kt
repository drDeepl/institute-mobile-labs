package com.example.lab5v2


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.slf4j.LoggerFactory
import org.slf4j.Logger


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PERMISSIONS: Int = 100
    private lateinit var textCoords: TextView
    private val logger: Logger = LoggerFactory.getLogger("MainActivity")
    private val TAG = this.javaClass.simpleName

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        locationRequest = LocationRequest()
        locationRequest.interval = 3000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 5f // 5m
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        locationCallback = object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?){
                locationResult ?: return
                if(locationResult.locations.isNotEmpty()){
                    val location = locationResult.lastLocation
                    textCoords.text = location.toString()

                }
            }

        }





        val btnFindTransport: Button = findViewById<Button>(R.id.btn_find_transport)
        btnFindTransport.setOnClickListener{

            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

    }

    private fun init(){
        Log.i(TAG, "INIT")
        textCoords = findViewById(R.id.text_coords)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }
    private fun isLocationEnabled(): Boolean{
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        Log.i(TAG, "CHECK PERMISSIONS")
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
}