package com.labs.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.labs.lab3.services.ModelMotionPoint

class ModelPointActivity : AppCompatActivity() {

    private val modelChoices: Map<Int,String> = mapOf(
       0 to "Движение под углом к горизонту",
        1 to  "свободное падение",
        )

    private val textEditOfModelPoint: Map<Int,Array<String>> = mapOf(
        0 to arrayOf("")
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model_point)

        val btnCalcPointCoord: Button = findViewById(R.id.btn_calc_time_fly)
        btnCalcPointCoord.setOnClickListener {
            val inputV0: EditText = findViewById(R.id.inputV0)
            val inputAlpha: EditText = findViewById(R.id.inputAlpha)

            val v0: Double = inputV0.text.toString().toDouble()
            val alpha: Double = inputAlpha.text.toString().toDouble()

            val modelMotionPoint: ModelMotionPoint = ModelMotionPoint(0.0, 0.0)
            val coords: Map<String, Double> = modelMotionPoint.AngleShot(v0, alpha)
            val textResultCoords: TextView = findViewById(R.id.resultCoords)
            textResultCoords.text ="x = " + coords.getValue("x") + "\ty = " + coords.getValue("y") + "\nt = " + coords.getValue(
                    "t"
                )
        }
        val modelChoisesSpinner: Spinner = findViewById(R.id.spinnerModels);
        val items: List<String> = listOf("Движение объекта, брошенного под углом", "Свободное падение")
        val arrayAdapter = ArrayAdapter<String>(this@ModelPointActivity, android.R.layout.simple_spinner_dropdown_item, items)
        modelChoisesSpinner.adapter = arrayAdapter
        modelChoisesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem: String = modelChoisesSpinner.selectedItem.toString()
                val modelPointTitle: TextView = findViewById(R.id.modelPointTitle)
                modelPointTitle.text = selectedItem

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("onNothingSelected")
            }
        }





    }

    private fun showText(text:String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}