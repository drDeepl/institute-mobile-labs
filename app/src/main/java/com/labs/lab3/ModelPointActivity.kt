package com.labs.lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.labs.lab3.services.ModelMotionPoint

class ModelPointActivity : AppCompatActivity() {

    private val modelChoices: Map<Int,String> = mapOf(
       0 to "движение под углом к горизонту",
        1 to  "свободное падение",
        )

    private val imagesTitleId: Map<String,Int> = mapOf(
        "движение объекта брошенного под углом" to 0,
        "свободное падение" to 1,
    )

    private val resultText: Map<Int,String> = mapOf(
        0 to "координаты точки:\nвремя полета:",
        1 to "время падения:"
    )

    private var currentImageId: Int = 0
    private var motionImages: Map<Int,String> = mapOf(0 to "shot", 1 to "free_fall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model_point)
        val inputV0: EditText = findViewById(R.id.inputV0)
        val inputAlpha: EditText = findViewById(R.id.inputAlpha)
        val inputH: EditText = findViewById(R.id.inputH)
        val btnCalcPointCoord: Button = findViewById(R.id.btn_calc_time_fly)
        val btnCalPriceCarActivity: Button = findViewById(R.id.btn_activity_car_price_calc)
        btnCalPriceCarActivity.setOnClickListener {
            val intent: Intent = Intent(this, CarPriceCalcActivity::class.java)
            startActivity(intent)
        }

        val btnCalcUtilityBills: Button = findViewById(R.id.btn_calc_utility_bills_activity)
        btnCalcUtilityBills.setOnClickListener {
            val intentBills: Intent = Intent(this, UtilityBillsActivity::class.java)
            startActivity(intentBills)
        }

        btnCalcPointCoord.setOnClickListener {
            val textResultCoords: TextView = findViewById(R.id.resultCoords)
            val modelMotionPoint: ModelMotionPoint = ModelMotionPoint(0.0, 0.0)
            if(currentImageId == 0){
                val v0: Double = inputV0.text.toString().toDouble()
                val alpha: Double = inputAlpha.text.toString().toDouble()
                val coords: Map<String, Double> = modelMotionPoint.AngleShot(v0, alpha)
                textResultCoords.text ="x = " + coords.getValue("x") + "\ty = " + coords.getValue("y") + "\nt = " + coords.getValue(
                    "t"
                )
            }
            else if(currentImageId == 1){
                val h: Double = inputH.text.toString().toDouble()
                val time: Double = modelMotionPoint.freeFall(h)
                textResultCoords.text = "t = " + time.toString()
            }

        }
        val modelChoisesSpinner: Spinner = findViewById(R.id.spinnerModels);
//        val items: List<String> = listOf("Движение объекта, брошенного под углом", "Свободное падение")
        val items: List<String> = imagesTitleId.keys.toList()
        val arrayAdapter = ArrayAdapter<String>(this@ModelPointActivity, android.R.layout.simple_spinner_dropdown_item, items)
        modelChoisesSpinner.adapter = arrayAdapter
        modelChoisesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val selectedItem: String = modelChoisesSpinner.selectedItem.toString().trim()

                val modelPointTitle: TextView = findViewById(R.id.modelPointTitle)
                modelPointTitle.text = selectedItem
                val selectedImageId: Int = imagesTitleId.getValue(selectedItem.lowercase())
                val resultTextTitle: TextView = findViewById(R.id.resultCoordsTitle)
                resultTextTitle.text = resultText.getValue(selectedImageId)
                currentImageId = selectedImageId
                setImage(selectedImageId)
                if(selectedImageId == 0){

                    inputAlpha.setVisibility(View.VISIBLE)
                    inputV0.setVisibility(View.VISIBLE)
                    inputH.setVisibility(View.GONE)
                }
                else{
                    inputAlpha.setVisibility(View.GONE)
                    inputV0.setVisibility(View.GONE)
                    inputH.setVisibility(View.VISIBLE)

                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("onNothingSelected")
            }
        }





    }

    private fun showText(text:String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun setImage(imageId: Int) {
        currentImageId = imageId
        val idImage: Int = resources.getIdentifier(motionImages.getValue(imageId), "drawable", packageName)
        val pointImage: ImageView = findViewById(R.id.imageMotionPoint)
        pointImage.setImageResource(idImage)
    }
}