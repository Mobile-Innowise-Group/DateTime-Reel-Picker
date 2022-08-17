package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.innowisegroup.reelpicker.datetime.LocalTime
import com.innowisegroup.reelpicker.picker.ReelPicker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reelPicker = ReelPicker.createTimeDialog(
            initialLocalTime = LocalTime.of(5, 32),
            minLocalTime = LocalTime.of(4, 31),
            maxLocalTime = LocalTime.of(6, 33)
        )

        val javaWrapper = JavaWrapper()

        val kotlinButton = findViewById<Button>(R.id.kotlinButton)
        val javaButton = findViewById<Button>(R.id.javaButton)

        kotlinButton.setOnClickListener {
            reelPicker.showDialog(supportFragmentManager)
        }
        javaButton.setOnClickListener {
            javaWrapper.showDialog(supportFragmentManager)
        }
    }
}