package com.example.lab5v2.api.models

import com.google.gson.annotations.SerializedName

data class Account(val id: Long, val username:String, val isAdmin: Boolean, val balance: Double)
