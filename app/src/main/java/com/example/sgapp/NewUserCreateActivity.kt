package com.example.sgapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.ActionBar

class NewUserCreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        var returnbutton = findViewById<ImageButton>(R.id.back_button)
        returnbutton.setOnClickListener{
            finish()
        }
    }
}