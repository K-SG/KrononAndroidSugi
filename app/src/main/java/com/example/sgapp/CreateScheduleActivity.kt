package com.example.sgapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class CreateScheduleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_schedule)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }
        val okButton = findViewById<Button>(R.id.ok_button)
        okButton.setOnClickListener{
            finish()
        }
        val dateText = findViewById<TextView>(R.id.date)
        dateText.setOnClickListener {
            showDatePicker()
        }
    }
    private fun showDatePicker() {
        var datepickerText = findViewById<TextView>(R.id.date)
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() {view, year, month, dayOfMonth->
                datepickerText.text = "選択した日付は「${year}/${month + 1}/${dayOfMonth}」です"
            },
            2020,
            3,
            1)
        datePickerDialog.show()
    }
}