package com.labs.lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.AndroidViewModel
import com.labs.lab3.api.CurrencyExchange
import com.labs.lab3.api.utils.Utils
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.text.FieldPosition


class SecondActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val btnMainActivity: Button = findViewById<Button>(R.id.btn_main_activity)
        btnMainActivity.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val btnCarPriceCalActivity: Button = findViewById<Button>(R.id.btn_activity_car_price_calc)
        btnCarPriceCalActivity.setOnClickListener {
            val intent: Intent = Intent(this, CarPriceCalcActivity::class.java)
            startActivity(intent)
        }

        val utils: Utils = Utils()

        var currencyExchange: CurrencyExchange = CurrencyExchange("RUB")
        var inputCurrency: EditText = findViewById(R.id.inputCurrencyValue)

        inputCurrency.hint = currencyExchange.getBaseCurrency()
        val currencys: List<String> = currencyExchange.getCurrencys()

        var currencySpinner: Spinner = findViewById(R.id.currencySpinner)

        val adapterCurrency: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item, currencys)

        adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner.setAdapter(adapterCurrency)
        var resultValueCurrency: TextView = findViewById(R.id.resultValueCurrency)

        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem: String = currencySpinner.selectedItem.toString()
                val currencyResultText: TextView = findViewById(R.id.resultValueCurrency)
                if(utils.isNumeric(inputCurrency.text.toString())){
                    val inputValue: Double = inputCurrency.text.toString().trim().toDouble()
                    currencyResultText.text =  currencyExchange.getExchange(selectedItem, inputValue).toString()
                }
                else{
                    currencyResultText.text = "Введенное значение не является числом!"
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("onNothingSelected")
            }
        }

    }
}