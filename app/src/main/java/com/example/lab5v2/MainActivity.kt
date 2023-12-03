package com.example.lab5v2


import android.Manifest
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
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import org.slf4j.LoggerFactory
import org.slf4j.Logger


class MainActivity : AppCompatActivity() {


    private val REQUEST_CODE_PERMISSIONS: Int = 100
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val logger: Logger = LoggerFactory.getLogger("MainActivity")
    private val TAG = this.javaClass.simpleName
    private lateinit var textCoords: TextView;
    private var currCoords: HashMap<String,Double?> = hashMapOf("lat" to null, "long" to null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnFindTransport: Button = findViewById(R.id.btn_find_transport)
        init()

        btnFindTransport.setOnClickListener {
            if (isGpsEnabled(baseContext)) {
                getLocation()
                if(currCoords.get("lat") != null && currCoords.get("long") != null){
                    Toast.makeText(this, "${currCoords.get("lat")}\t${currCoords.get("long")}", Toast.LENGTH_LONG).show()
                    val intent: Intent = Intent(this, MapActivity::class.java)
                    intent.putExtra("lat", currCoords.get("lat"))
                    intent.putExtra("long", currCoords.get("long"))
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Что-то пошло не так. Повтори попытку чуть позже", Toast.LENGTH_LONG).show()
                }


            } else {
                Toast.makeText(this, "GPS is off", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun init() {
        logger.info("INIT")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        textCoords = findViewById<TextView>(R.id.text_coords)
        getLocation()
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_CODE_PERMISSIONS
            )
        }

        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: Location? ->
                if (location == null){
                    Toast.makeText(this, "Не удаётся получить текущие координаты", Toast.LENGTH_SHORT).show()

                }
                else {
                    val lat = location.latitude
                    val lon = location.longitude
//                    Toast.makeText(this, "${lat}\t${lon}", Toast.LENGTH_LONG).show()
                    currCoords.put("lat", lat)
                    currCoords.put("long", lon)
                }
        }
    }

    private fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}