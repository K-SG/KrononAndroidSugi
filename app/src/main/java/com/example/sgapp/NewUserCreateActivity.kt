package com.example.sgapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class NewUserCreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        var returnButton = findViewById<ImageButton>(R.id.back_button)
        var returnLogin = findViewById<TextView>(R.id.new_login)
        var createUserButton = findViewById<Button>(R.id.create_button)
        returnButton.setOnClickListener{
            finish()
        }
        returnLogin.setOnClickListener{
            finish()
        }
        createUserButton?.setOnClickListener{
            val intent = Intent(this, MainButtomNavigationActivity::class.java)
            startActivity(intent)
        }
    }
}