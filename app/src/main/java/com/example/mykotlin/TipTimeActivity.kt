package com.example.mykotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mykotlin.databinding.ActivityTipTimeBinding
import java.text.NumberFormat
import kotlin.math.ceil

class TipTimeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTipTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calculateButton.setOnClickListener { calculateTip() }
    }

    private fun calculateTip() {
        val value = binding.costOfService.text.toString()
        val cost = value.toDoubleOrNull()
        if (cost == null || cost == 0.0) {
            displayTip(0.0)
            return
        }
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_15_percent -> 0.15
            R.id.option_18_percent -> 0.18
            else -> 0.20
        }
        var tip = cost * tipPercentage
        if (binding.roundUpSwitch.isChecked) {
            tip = ceil(tip)
        }
        displayTip(tip)
    }

    private fun displayTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }
}