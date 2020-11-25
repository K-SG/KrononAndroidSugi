package com.example.sgapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var start_button = findViewById<ImageView>(R.id.start_button)
        start_button.setOnClickListener {
            val intent = Intent(this, MainButtomActivity::class.java)
            startActivity(intent)
        }
    }
}