package com.example.lab1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }

    fun onClickBtnPrev(v: View){
        println("onClickBtnNext")
        var intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}