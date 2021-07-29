package com.example.mykotlin

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice)

        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener {
            rollDice()
        }

        rollDice()
    }

    private fun rollDice() {
        val dice = Dice(6)
        val diceRoll = dice.roll()
        val imageView = findViewById<ImageView>(R.id.imageView)
        val resource = when(diceRoll) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        imageView.setImageResource(resource)
        //便于屏幕阅读器
        imageView.contentDescription = diceRoll.toString()

        var text = "You didn't win, try again!"
        if (diceRoll == 4) {
            text = "You win!"
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}