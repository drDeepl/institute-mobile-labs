package com.labs.lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.labs.lab3.api.utils.Utils

class UtilityBillsActivity : AppCompatActivity() {

    private val tariffs: Map<String, Int> = mapOf(
        "холодное водоснабжение" to 0,
        "водоотведение" to 1,
        "горячее водоснабжение" to 2,
        "электро-энергия" to 3,
        "отопление" to 4,
        "тко" to 5,
    )

    private val utilityTariffs: Map<Int, Double> = mapOf(
        0 to 33.19,
        1 to 22.36,
        2 to 65.60,
        3 to 2.64,
        4 to 27.05,
        5 to 97.84
    )

    private var tariffsHints: Map<Int,String> = mapOf(
        0 to "руб./м3",
        1 to "руб./м3",
        2 to "руб./м3",
        3 to "руб./кВт*ч",
        4 to "руб./м2",
        5 to "руб./чел"
    )

    private var calclulateTariff: MutableMap<String,Boolean> = mutableMapOf(
        "холодное водоснабжение" to false,
        "водоотведение" to false,
        "отопление" to false,
        "горячее водоснабжение" to false,
        "электро-энергия" to false,
        "тко" to false,
    )

    var summBills:Double = 0.0
    var currentBillActive: String = " ";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utility_bills)

        val btnModelMotionPointActivity: Button = findViewById(R.id.btn_model_motion_point_activity)
        btnModelMotionPointActivity.setOnClickListener {
            val intentMotionPoint: Intent = Intent(this, ModelPointActivity::class.java)
            startActivity(intentMotionPoint)
        }

        val areaEdit: EditText = findViewById(R.id.inputApartamentArea)
        val countPeople:EditText = findViewById(R.id.inputCountPeople)
        val meterReading:EditText = findViewById(R.id.inputMeterReading)
        areaEdit.setVisibility(View.GONE)
        countPeople.setVisibility(View.GONE)
        meterReading.setVisibility(View.GONE)


        val warmBill: CheckBox = findViewById(R.id.warmBill)
        val tkoBill: CheckBox = findViewById(R.id.tkoBill)
        val billsItems: List<String> = tariffs.keys.toList().slice(0..3)
        currentBillActive = billsItems[0]
        val spinnerBills: Spinner = findViewById(R.id.billForMeaterReadingSpinner)
        val adapterBills: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item, billsItems)

        adapterBills.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBills.setAdapter(adapterBills)

        spinnerBills.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                println("onItemSelected")
                val selectedItem: String = spinnerBills.selectedItem.toString()
                val tariffId: Int = tariffs.getValue(selectedItem)
                calclulateTariff.put(currentBillActive, false)
                currentBillActive = selectedItem
                val price: String = utilityTariffs.getValue(tariffId).toString()
                val tariffValue: String = tariffsHints.getValue(tariffId).toString()
//                currentOptionView.text = selectedItem + "\nтариф " + price + " " + tariffValue
                calclulateTariff.put(selectedItem, true)
                meterReading.hint = "показатели счетчика "  + tariffsHints.getValue(tariffId).toString().split("/")[1]
                changeActiveBills(meterReading, true)

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("onNothingSelected")
                for(i in 0..<billsItems.size){
                    calclulateTariff.put(billsItems[i], false)
                }
                changeActiveBills(meterReading, false)
                val resultText: TextView = findViewById(R.id.resultValue)
                resultText.text = ""

            }
        }


        warmBill.setOnCheckedChangeListener{_,isChecked->
            calclulateTariff.put(warmBill.text.toString(), isChecked)




            changeActiveBills(areaEdit,isChecked)
        }

        tkoBill.setOnCheckedChangeListener{_,isChecked->
            calclulateTariff.put(tkoBill.text.toString(), isChecked)



            changeActiveBills(countPeople,isChecked)
        }

        val btnCalcBill: Button = findViewById(R.id.btnCalcBill)
        btnCalcBill.setOnClickListener {
            val resultText: TextView = findViewById(R.id.resultValue)
            val utils: Utils = Utils()
            summBills = 0.0
            resultText.text = ""
            val bills: List<String> = calclulateTariff.keys.toList()
            for(i in 0..<bills.size){
                val billName: String = bills[i]
                val tariffIsChoice: Boolean = calclulateTariff.getValue(billName)
                if(tariffIsChoice){
                    val tariffId:Int = tariffs.getValue(billName)
                    val price: Double = utilityTariffs.getValue(tariffId)
                    if(tariffId <= 3){
                        val meterReadingValue: String = meterReading.text.toString()
                        if(utils.isNumeric(meterReadingValue)){
                            summBills += meterReadingValue.toDouble() * price

                        }
                    }
                    else if(tariffId == 4){
                        val areaEditValue: String = areaEdit.text.toString()
                        if(utils.isNumeric(areaEditValue)){
                            summBills += areaEditValue.toDouble() * price
                        }
                    }
                    else if(tariffId == 5){
                        val countPeopleValue: String = countPeople.text.toString()
                        if(utils.isNumeric(countPeopleValue)){
                            summBills += countPeopleValue.toInt() * price
                        }

                    }
                }
            }

            resultText.text = "Итого к оплате:\n" + utils.roundTo2Number(summBills).toString() + " руб."

        }

    }

    private fun changeActiveBills(targetTextEdit: EditText, isChecked: Boolean){
        if(isChecked){
            targetTextEdit.setVisibility(View.VISIBLE)
        }
        else{
            targetTextEdit.setVisibility(View.GONE)
        }


    }
}