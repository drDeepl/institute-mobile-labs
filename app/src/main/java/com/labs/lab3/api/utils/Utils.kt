package com.labs.lab3.api.utils

class Utils {

    fun isNumeric(str: String): Boolean{
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return str.matches(regex)
    }
}