package com.example.mykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class FirstActivity : AppCompatActivity() {

    private val LOG_TAG = "test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val textView = findViewById <TextView>(R.id.textView_one)
//        "hello android".also { textView.text = it }

        val btn = findViewById<Button>(R.id.button_1)
        btn.setOnClickListener {
            Log.e(LOG_TAG, "onClick...")
//            startActivity(Intent(this, CardActivity::class.java))
//            startActivity(Intent(this, DiceActivity::class.java))
            startActivity(Intent(this, TipTimeActivity::class.java))
        }
        btn.setOnLongClickListener {
            Log.e(LOG_TAG, "OnLongClick...")
            return@setOnLongClickListener false
        }
//        btn.setOnTouchListener {
//            Log.e(LOG_TAG, "OnTouch...")
//            return@setOnTouchListener false
//        }
    }
}