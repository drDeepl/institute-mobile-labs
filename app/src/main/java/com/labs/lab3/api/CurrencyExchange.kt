package com.labs.lab3.api

import okhttp3.internal.connection.Exchange
import java.util.Currency

class CurrencyExchange(private var baseCurrency: String ) {

    private val currencys: Map<String,Double> = mapOf(
        "USD" to 98.0,
        "EUR" to 103.25,
        "ETH" to 165.223,
        "CNY" to 13.36,
        "GBP" to 118.83,

    )

    fun getBaseCurrency(): String{
        return baseCurrency
    }

    fun getCurrencys(): List<String>{
        return currencys.keys.toList()

    }

    fun getExchange(exchangeName: String, value:Double): Double{
        println(exchangeName)
        return currencys.getValue(exchangeName) * value
    }

}