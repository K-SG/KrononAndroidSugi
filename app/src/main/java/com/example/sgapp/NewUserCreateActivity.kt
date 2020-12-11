package com.example.sgapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class NewUserCreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        var returnButton = findViewById<ImageButton>(R.id.back_button)
        var returnLoginText = findViewById<TextView>(R.id.new_login)
        returnButton.setOnClickListener{
            finish()
        }
        returnLoginText.setOnClickListener{
            finish()
        }
    }
}