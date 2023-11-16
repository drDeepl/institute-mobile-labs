package com.example.lab5v2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab5v2.databinding.ActivityMainBinding
import com.google.android.gms.common.api.internal.ApiKey
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val API_KEY: String = "4b11185e-0afc-49bc-8a99-909376f84f67"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setApiKey(savedInstanceState)
        MapKitFactory.initialize(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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