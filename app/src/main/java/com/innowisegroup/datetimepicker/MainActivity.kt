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

        val dateTime = findViewById<Button>(R.id.dateTime)
        val onlyTime = findViewById<Button>(R.id.timeOnly)
        val onlyDate = findViewById<Button>(R.id.dateOnly)

        val tvDateTime = findViewById<TextView>(R.id.tvDateTime)
        val tvDateOnly = findViewById<TextView>(R.id.tvDateOnly)
        val tvTimeOnly = findViewById<TextView>(R.id.tvTimeOnly)

        dateTime.setOnClickListener {
            ReelPicker
                .createDateTimeDialog()
                .setOkClickCallback(object : ReelPicker.OkClickCallback<LocalDateTime> {
                    override fun onOkClick(value: LocalDateTime) {
                        tvDateTime.text =
                            "${value.toLocalDate().year}/${value.toLocalDate().month}/${value.toLocalDate().day} | " +
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
                        tvDateOnly.text = "${value.year}/${value.month}/${value.day}"
                    }
                })
                .setCancelClickCallback(object : ReelPicker.CancelClickCallback {
                    override fun onCancelClick() {}
                })
                .showDialog(supportFragmentManager)
        }
        onlyTime.setOnClickListener {
            ReelPicker
                .createTimeDialog()
                .setOkClickCallback(object : ReelPicker.OkClickCallback<LocalTime> {
                    override fun onOkClick(value: LocalTime) {
                        tvTimeOnly.text = "${value.hour}:${value.minute}"
                    }
                })
                .setCancelClickCallback(object : ReelPicker.CancelClickCallback {
                    override fun onCancelClick() {}
                })
                .showDialog(supportFragmentManager)
        }
    }
}