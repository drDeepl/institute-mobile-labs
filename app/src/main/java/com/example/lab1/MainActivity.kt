package com.example.lab1

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nameButton: Button = findViewById(R.id.btn_name)
        nameButton.setOnClickListener{
            toChangeText("01.03.02")

        }

        val diceBtn: Button = findViewById(R.id.btnDice)
        diceBtn.setOnClickListener {
            rollDice()
        }
    }

    private fun toChangeText(text: String){
        var textCat: TextView = findViewById(R.id.textCat)
        textCat.text = text
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }


    private fun rollDice() {
        val random: Random = Random();
        val randomInt: Int = random.nextInt(6)+1;
        val drawableResource = when (randomInt) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        val diceImage: ImageView = findViewById(R.id.diceImage)
        diceImage.setImageResource(drawableResource)
    }

}