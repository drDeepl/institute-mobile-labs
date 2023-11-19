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
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lab5v2.databinding.ActivityMainBinding
import com.example.lab5v2.service.RentLocationListener
import com.example.lab5v2.service.interfaces.LocationListenerI
import com.google.android.gms.common.api.internal.ApiKey
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import mu.KotlinLogging
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val REQUEST_CODE_PERMISSIONS: Int = 100
    private lateinit var rentLocationListener: RentLocationListener
    private val secondsToGetGps: Long = 2
    private val distanceToGetGps: Float = 1.0f
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private lateinit var textCoords: TextView
    private val logger: Logger = LoggerFactory.getLogger("MainActivity")
    private val TAG = this.javaClass.simpleName




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()


        val btnFindTransport: Button = findViewById<Button>(R.id.btn_find_transport)
        btnFindTransport.setOnClickListener{
//            fetchLocation()

            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

    }

    private fun init(){
        Log.i(TAG, "INIT")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        textCoords = findViewById(R.id.text_coords)



    }

    private fun fetchLocation(){
        Log.i(TAG, "FETCH LOCATION")
        val taskLocation: Task<Location> = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_PERMISSIONS)
        }

        taskLocation.addOnSuccessListener {
            if(it != null){
                textCoords.text = "${it.latitude} ${it.longitude}"
                Toast.makeText(applicationContext, "${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}