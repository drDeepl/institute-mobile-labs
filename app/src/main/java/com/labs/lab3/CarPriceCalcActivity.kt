package com.labs.lab3

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import org.w3c.dom.Text
import java.util.Random

class CarPriceCalcActivity : AppCompatActivity() {
    private val priceOption: Double = 15000.0
    private val carsPrice: Map<Int, Double> = mapOf(
        0 to 760300.0,
        1 to 1800800.0,
    )
    private val carsName: Map<Int,String> = mapOf(
        0 to "Ford Focus",
        1 to "Tesla S"
    )
    private val carsImage: Map<Int,String> = mapOf(
        0 to "ford_focus",
        1 to "tesla_s"
    )

    private var currentCarId: Int = 0
    private val currencyPrice:String = " руб."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_price_calc)

        val btnToCalcCurrency: Button = findViewById(R.id.btn_cal_currency_activity)
        btnToCalcCurrency.setOnClickListener {
            var intent: Intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        var nameCar: TextView = findViewById(R.id.nameCar)
        var priceCar: TextView = findViewById(R.id.priceCar)
        setCar(nameCar,priceCar,currentCarId);

        val carOptionSignal: CheckBox = findViewById(R.id.carOptionSignal)
        val carOptionClimate: CheckBox = findViewById(R.id.carOptionClimate)
        val carOptionCamera: CheckBox = findViewById(R.id.carOptionCamera)
        val carOptionTires: CheckBox = findViewById(R.id.carOptionTires)

        val btnNextImage: Button = findViewById(R.id.btn_next_car)
        btnNextImage.setOnClickListener {
            setCar(nameCar,priceCar, 1)
        }

        val btnPrevImage: Button = findViewById(R.id.btn_prev_car)
        btnPrevImage.setOnClickListener {
            setCar(nameCar,priceCar, 0)
        }

        carOptionSignal.setOnCheckedChangeListener{_,isChecked->
            changePrice(priceCar,nameCar, isChecked)
        }
        carOptionClimate.setOnCheckedChangeListener{_,isChecked->
            changePrice(priceCar,nameCar, isChecked)
        }
        carOptionCamera.setOnCheckedChangeListener{_,isChecked->
            changePrice(priceCar,nameCar, isChecked)
        }
        carOptionTires.setOnCheckedChangeListener{_,isChecked->
            changePrice(priceCar,nameCar, isChecked)
        }


    }

    private fun setCar(nameCarTextView: TextView, priceCarTextView:TextView, carId: Int) {
        currentCarId = carId
        val idImage: Int = resources.getIdentifier(carsImage.getValue(carId), "drawable", packageName)
//        val drawableResource: Int = R.drawable.tesla_s
        nameCarTextView.text = carsName.getValue(carId)
        priceCarTextView.text = Math.round(carsPrice.getValue(carId)).toString() + currencyPrice
        val carImage: ImageView = findViewById(R.id.image_preview_car)
        carImage.setImageResource(idImage)
    }


    private fun changePrice(priceCar: TextView, nameCarTextView: TextView, isChecked: Boolean){
        val currentPrice: Double = priceCar.text.toString().split(" ")[0].toDouble()
        if(isChecked){
            val newPrice: Double = currentPrice + priceOption
            priceCar.text = newPrice.toString() + currencyPrice
        }
        else{
            val newPrice =  currentPrice - priceOption
            priceCar.text = newPrice.toString() + currencyPrice
        }
    }






}