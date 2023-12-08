package com.example.lab5v2.api.models

import com.google.gson.annotations.SerializedName

data class Account(val type: String, val token:String, val refreshToken:String)
