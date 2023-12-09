package com.example.lab5v2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lab5v2.api.ServiceBuilder
import com.example.lab5v2.api.interfaces.ApiInterface
import com.example.lab5v2.api.models.AccountSignInModel
import com.example.lab5v2.api.models.TokenModel
import com.example.lab5v2.databinding.ActivityMapBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView


class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private val API_KEY: String = "4b11185e-0afc-49bc-8a99-909376f84f67"
    private lateinit var mapView: MapView
    private lateinit var mapKit: MapKit
    private lateinit var pointOnMap: PlacemarkMapObject



    override fun onCreate(savedInstanceState: Bundle?) {
        initMap()
        super.onCreate(savedInstanceState)
        val currentLatitude: Double = intent.getDoubleExtra("lat", 0.0)
        val currentLongitude: Double = intent.getDoubleExtra("long", 0.0)
        pointOnMap = mapView.map.mapObjects.addPlacemark(
            Point(currentLatitude, currentLongitude)
        )
        pointOnMap.setIconStyle(IconStyle().setScale(3f))
        mapView.map.move(CameraPosition(Point(currentLatitude, currentLongitude), 17f, 0f, 0f), Animation(Animation.Type.SMOOTH, 10f), null)
        mainInit()



    }


    private fun mainInit(){
        val navigationBottom: BottomNavigationView = findViewById(R.id.bottom_navigation)
        navigationBottom.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.to_main_activity -> {
                    val intent: Intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }
    private fun initMap(){
        setApiKey()
        MapKitFactory.initialize(this)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapKit = MapKitFactory.getInstance()
        mapView = findViewById(R.id.mapview)
    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putBoolean("haveApiKey", true)
    }

    private fun setApiKey(){
        MapKitFactory.setApiKey(API_KEY)
    }

    override fun onStart() {

        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
        super.onStart()
    }

    // Останавливаем обработку карты, когда активити с картой становится невидимым для пользователя:
    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()

    }

    private fun toProfileActivity(){
        val intent: Intent = Intent(this, UserAccountActivity::class.java)
        startActivity(intent)

    }
    private fun toMainActivity(){
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }
}