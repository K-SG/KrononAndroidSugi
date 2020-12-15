package com.example.sgapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class UpdateScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_schedule)
        var backButton = findViewById<ImageView>(R.id.back_button)
        var okButton = findViewById<Button>(R.id.ok_button)
        backButton.setOnClickListener{
            finish()
        }
        okButton.setOnClickListener{
            finish()
        }
    }
}