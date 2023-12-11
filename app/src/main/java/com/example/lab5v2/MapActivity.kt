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
import com.yandex.runtime.image.ImageProvider
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


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

        val currentLocationPoint: Point = Point(currentLatitude, currentLongitude)
        val points: List<Point> = generateRandomPointsAroundCurrentPoint(currentLocationPoint, 0.002, 5)
        pointOnMap.setIconStyle(IconStyle().setScale(3f))

        for(i in 0 until points.size){
            val transportPoint: Point = points.get(i)


            var mark: PlacemarkMapObject = mapView.map.mapObjects.addPlacemark(transportPoint)
            mark.setIcon(ImageProvider.fromResource(this, R.drawable.auto))
            mark.setIconStyle(IconStyle().setScale(0.1F))

        }
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

    private fun generateRandomPointsAroundCurrentPoint(centerPoint: Point, radius:Double, numPoints: Int): List<Point>{
        val points = mutableListOf<Point>()
        val random = java.util.Random()
        for (i in 0 until numPoints) {
            val angle = random.nextDouble() * 2 * PI
            val distance = random.nextDouble() * radius

            val xOffset = distance * cos(angle)
            val yOffset = distance * sin(angle)

            val newPoint = Point(centerPoint.latitude + xOffset, centerPoint.longitude + yOffset)
            points.add(newPoint)
        }

        return points
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