package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.innowisegroup.reelpicker.datetime.LocalDateTime
import com.innowisegroup.reelpicker.datetime.LocalTime
import com.innowisegroup.reelpicker.picker.ReelPicker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvResult = findViewById<TextView>(R.id.result)

        val javaWrapper = JavaWrapper()

        val kotlinButton = findViewById<Button>(R.id.kotlinButton)
        val javaButton = findViewById<Button>(R.id.javaButton)

        kotlinButton.setOnClickListener {
            ReelPicker.createDateTimeDialog().setOkClickCallback(object : ReelPicker.OkClickCallback<LocalDateTime>{
                override fun onOkClick(value: LocalDateTime) {
                    tvResult.text = "${value.toLocalDate().year}"
                }
            }).showDialog(supportFragmentManager)        }
        javaButton.setOnClickListener {
            javaWrapper.showDialog(supportFragmentManager)
        }
    }
}