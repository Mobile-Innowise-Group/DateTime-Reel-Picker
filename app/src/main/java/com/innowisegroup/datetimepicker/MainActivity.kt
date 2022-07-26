package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidThreeTen.init(this)
        val dateTimePickerDialog = DateTimePickerDialog(
            null, null, null,
            wrapSelectionWheel = false,
            withOnlyDatePicker = false,
            withOnlyTimePicker = false
        )
        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            dateTimePickerDialog.showDialog(supportFragmentManager)
        }
    }
}