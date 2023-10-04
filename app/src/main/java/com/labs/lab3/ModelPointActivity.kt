package com.labs.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.labs.lab3.services.ModelMotionPoint

class ModelPointActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model_point)

        val btnCalcPointCoord: Button = findViewById(R.id.btn_calc_time_fly)
        btnCalcPointCoord.setOnClickListener {
            val inputV0: EditText = findViewById(R.id.inputV0)
            val inputAlpha: EditText = findViewById(R.id.inputAlpha)
            val inputFlyTime: EditText = findViewById(R.id.inputFlyTime)
            val v0: Double = inputV0.text.toString().toDouble()
            val alpha: Double = inputAlpha.text.toString().toDouble()
            val t: Int = inputFlyTime.text.toString().toInt()
            val modelMotionPoint: ModelMotionPoint = ModelMotionPoint(0.0,0.0)
            val coords: Map<String,Double> = modelMotionPoint.AngleShot(v0,alpha, t)
            val textResultCoords: TextView = findViewById(R.id.resultCoords)
            val textResultCoordsTitle: TextView = findViewById(R.id.resultCoordsTitle)
            textResultCoords.text = "x = " + coords.getValue("x") + "y = " + coords.getValue("y")



        }
    }
}