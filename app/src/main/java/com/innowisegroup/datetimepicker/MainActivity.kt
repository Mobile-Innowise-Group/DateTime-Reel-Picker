package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.innowisegroup.reelpicker.datetime.LocalDate
import com.innowisegroup.reelpicker.datetime.LocalDateTime
import com.innowisegroup.reelpicker.datetime.LocalTime
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
                        tvResult.text = "${value.toLocalDate().year}/${value.toLocalDate().month}/${value.toLocalDate().day} | "+
                                "${value.toLocalTime().hour}:${value.toLocalTime().minute}"
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
            ReelPicker
                .createTimeDialog()
                .setOkClickCallback(object : ReelPicker.OkClickCallback<LocalTime> {
                    override fun onOkClick(value: LocalTime) {
                        tvResult.text = "${value.hour}:${value.minute}"
                    }
                })
                .showDialog(supportFragmentManager)
        }
    }
}