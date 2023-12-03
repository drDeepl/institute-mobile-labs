package com.example.lab5v2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lab5v2.databinding.ActivityMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private val API_KEY: String = "4b11185e-0afc-49bc-8a99-909376f84f67"
    private lateinit var mapView: MapView
    private lateinit var mapKit: MapKit
    private lateinit var pointOnMap: PlacemarkMapObject


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMap(savedInstanceState)

        val currentLatitude: Double = intent.getDoubleExtra("lat", 0.0)
        val currentLongitude: Double = intent.getDoubleExtra("long", 0.0)
        pointOnMap = mapView.map.mapObjects.addPlacemark(
            Point(currentLatitude, currentLongitude)
        )

        pointOnMap.setIconStyle(IconStyle().setScale(3f))



        mapView.map.move(CameraPosition(Point(currentLatitude, currentLongitude), 17f, 0f, 0f), Animation(Animation.Type.SMOOTH, 10f), null)




    }


    private fun initMap(savedInstanceState: Bundle?){
        setApiKey(savedInstanceState)
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

    private fun setApiKey(savedInstanceState: Bundle?){
        val haveApiKey = savedInstanceState?.getBoolean("haveApiKey") ?: false

        if(!haveApiKey){
            MapKitFactory.setApiKey(API_KEY)
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    // Останавливаем обработку карты, когда активити с картой становится невидимым для пользователя:
    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}