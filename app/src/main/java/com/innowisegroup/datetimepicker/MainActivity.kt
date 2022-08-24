package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.innowisegroup.reelpicker.datetime.LocalDate
import com.innowisegroup.reelpicker.picker.ReelPicker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reelPicker = ReelPicker.createDateDialog(
            initialLocalDate = LocalDate.of(28, 2,1900),
            minLocalDate = LocalDate.of(27, 2,1900),
            maxLocalDate = LocalDate.of(28, 2,1900)
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