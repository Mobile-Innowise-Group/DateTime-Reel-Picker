package com.innowisegroup.datetimepicker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.innowisegroup.reelpicker.picker.ReelPicker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reelPicker = ReelPicker.createDateTimeDialog()

        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            reelPicker.showDialog(supportFragmentManager)
        }
    }
}