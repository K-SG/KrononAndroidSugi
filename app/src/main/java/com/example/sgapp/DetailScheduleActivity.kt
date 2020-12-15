package com.example.sgapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView

class DetailScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_schedule)
        var backButton = findViewById<ImageView>(R.id.back_button)
        var editScheduleButton = findViewById<ImageButton>(R.id.update_button)
        var deleteScheduleButton = findViewById<ImageButton>(R.id.delete_button)

        backButton.setOnClickListener{
            finish()
        }
        editScheduleButton.setOnClickListener{
            val intent = Intent(this,UpdateScheduleActivity::class.java)
            startActivity(intent)
        }
        deleteScheduleButton.setOnClickListener{
            finish()
        }

    }
}