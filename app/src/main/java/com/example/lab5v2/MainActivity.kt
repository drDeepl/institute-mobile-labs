package com.example.lab5v2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Half.toFloat
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lab5v2.databinding.ActivityMainBinding
import com.example.lab5v2.service.RentLocationListener
import com.example.lab5v2.service.interfaces.LocationListenerI
import com.google.android.gms.common.api.internal.ApiKey
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity(), LocationListenerI {
    private lateinit var locationManager: LocationManager
    private val REQUEST_CODE_PERMISSIONS: Int = 100
    private lateinit var rentLocationListener: RentLocationListener
    private val secondsToGetGps: Long = 2
    private val distanceToGetGps: Float = 1.0f
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private lateinit var textCoords: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()


        val btnFindTransport: Button = findViewById<Button>(R.id.btn_find_transport)
        btnFindTransport.setOnClickListener{
            checkPermission()
//            val intent = Intent(this, MapActivity::class.java)
//            startActivity(intent)
        }

    }

    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
//        if(location.hasSpeed()){
            currentLatitude = location.latitude
            currentLongitude = location.longitude
        findViewById<TextView>(R.id.text_coords).text = "Latitude: $currentLatitude\nLongitude: $currentLongitude"
//            Toast.makeText(this, "Latitude: $currentLatitude\nLongitude: $currentLongitude", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun init(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        rentLocationListener = RentLocationListener()
        rentLocationListener.setLocationListenerI(this)
        checkPermission()
        textCoords = findViewById(R.id.text_coords)

    }
    private fun checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf<String>(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_PERMISSIONS)
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, secondsToGetGps,distanceToGetGps, rentLocationListener)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_PERMISSIONS && grantResults[0] == RESULT_OK){
            checkPermission()
        }
        else{
            Toast.makeText(this, "для получения транспорта рядом с вами необходимо дать разрешение на определение местоположения", Toast.LENGTH_SHORT).show()

        }
    }

}