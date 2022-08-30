package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.innowisegroup.reelpicker.datetime.LocalDate
import com.innowisegroup.reelpicker.datetime.LocalDateTime
import com.innowisegroup.reelpicker.picker.ReelPicker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvResult = findViewById<TextView>(R.id.result)

        val javaWrapper = JavaWrapper()

        val dateTime = findViewById<Button>(R.id.dateTime)
        val onlyTime = findViewById<Button>(R.id.onlyTime)
        val onlyDate = findViewById<Button>(R.id.onlyDate)

        dateTime.setOnClickListener {
            ReelPicker
                .createDateTimeDialog()
                .setOkClickCallback(object : ReelPicker.OkClickCallback<LocalDateTime> {
                    override fun onOkClick(value: LocalDateTime) {
                        tvResult.text = "${value.toLocalDate().year}"
                    }
                })
                .showDialog(supportFragmentManager)
        }
        onlyDate.setOnClickListener {
            ReelPicker
                .createDateDialog()
                .setOkClickCallback(object : ReelPicker.OkClickCallback<LocalDate> {
                    override fun onOkClick(value: LocalDate) {
                        tvResult.text = "${value.year}/${value.month}/${value.day}"
                    }
                })
                .showDialog(supportFragmentManager)
        }
        onlyTime.setOnClickListener {
            javaWrapper.showDialog(supportFragmentManager)
        }
    }
}