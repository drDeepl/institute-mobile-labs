package com.example.lab5v2


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.provider.Settings
import androidx.appcompat.app.AlertDialog

import com.example.lab5v2.service.TokenService

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.bottomnavigation.BottomNavigationView

import org.slf4j.LoggerFactory
import org.slf4j.Logger


class MainActivity : AppCompatActivity() {


    private val REQUEST_CODE_PERMISSIONS: Int = 100
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val LOGGER: Logger = LoggerFactory.getLogger("MainActivity")
    private val TAG = this.javaClass.simpleName
    private lateinit var tokenService: TokenService;
    private val REQUEST_CODE_GPS_SETTINGS = 101



    private var currCoords: HashMap<String,Double?> = hashMapOf("lat" to null, "long" to null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnFindTransport: Button = findViewById(R.id.btn_find_transport)
        btnFindTransport.setOnClickListener {
            if(isGpsEnabled(baseContext)){
                getCurrentLocation()
                setCurrentLocationOnMap()
                }
            else{
                showEnableGPSDialog()

            }

        }

        val navigationBottom: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navigationBottom.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.to_find_transport -> {
                    setCurrentLocationOnMap()
                }
                R.id.to_profile-> toProfileActivity()
                R.id.to_main_activity -> toMainActivity()
            }
            true
        }
        init()
    }

    private fun setCurrentLocationOnMap(){

            if(currCoords.get("lat") != null && currCoords.get("long") != null){
                Toast.makeText(this, "${currCoords.get("lat")}\t${currCoords.get("long")}", Toast.LENGTH_LONG).show()
                val intent: Intent = Intent(this, MapActivity::class.java)
                intent.putExtra("lat", currCoords.get("lat"))
                intent.putExtra("long", currCoords.get("long"))
                startActivity(intent)
            }
    }


    private fun showEnableGPSDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Упс, у вас выключено отображение локации")
            .setMessage("Чтобы показать ближайший транспорт доступный для аренды необходимо включить гео-локацию.\n\nПереключиться на страницу для включения локации? ")
            .setCancelable(false)
            .setPositiveButton("Да") { dialog, _ ->
                val enableGPSIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(enableGPSIntent, REQUEST_CODE_GPS_SETTINGS)
                dialog.dismiss()
            }
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
                // Handle scenario when user chooses not to enable GPS
            }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun toProfileActivity(){
        val intent: Intent = Intent(this, UserAccountActivity::class.java)
        startActivity(intent)

    }
    private fun toMainActivity(){
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }




    private fun init() {
        LOGGER.info("INIT")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        tokenService = TokenService(this)
        findViewById<TextView>(R.id.accessTokenTest).text = tokenService.getAccessToken()
    }


    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
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
                    currCoords.put("lat", lat)
                    currCoords.put("long", lon)
                }
        }
    }

    private fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_CODE_GPS_SETTINGS
        )
    }
    override fun onResume() {
        super.onResume()

        // Проверяем разрешение на доступ к местоположению
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Разрешение не получено, запрашиваем его
            requestLocationPermission()
        } else {
            // Разрешение получено, выполняем необходимые действия
            setCurrentLocationOnMap()
        }
    }
}