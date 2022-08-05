package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dateTimePickerDialog = DateTimePickerDialog.createDateTimePickerDialog()

        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            dateTimePickerDialog.showDialog(supportFragmentManager)
        }
    }
}