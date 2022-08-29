package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.innowisegroup.reelpicker.datetime.LocalDateTime
import com.innowisegroup.reelpicker.picker.ReelPicker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val kotlinButton = findViewById<Button>(R.id.kotlinButton)
        val javaButton = findViewById<Button>(R.id.javaButton)

        kotlinButton.setOnClickListener {
            ReelPicker
                .createDateTimeDialog()
                .setOnClickCallback(object : ReelPicker.OkClickCallback<LocalDateTime> {
                    override fun onOkClick(value: LocalDateTime) {
                        Log.e(
                            "LocalDateTime",
                            "${value.toLocalTime().hour}:${value.toLocalTime().minute}"
                        )
                        Log.e(
                            "LocalDateTime",
                            "${value.toLocalDate().day}/${value.toLocalDate().month}/${value.toLocalDate().year}"
                        )
                    }
                })
                .showDialog(supportFragmentManager)
        }
        javaButton.setOnClickListener {
            JavaWrapper().reelPicker.showDialog(supportFragmentManager)
        }
    }
}