package com.labs.lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class UtilityBillsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utility_bills)

        val btnModelMotionPointActivity: Button = findViewById(R.id.btn_model_motion_point_activity)
        btnModelMotionPointActivity.setOnClickListener {
            val intentMotionPoint: Intent = Intent(this, ModelPointActivity::class.java)
            startActivity(intentMotionPoint)
        }
    }
}