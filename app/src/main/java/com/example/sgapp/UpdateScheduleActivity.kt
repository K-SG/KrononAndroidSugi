package com.example.sgapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.retrofit2_kotlin.Retrofit2.KrononService
import com.example.sgapp.api.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.String
import java.text.SimpleDateFormat
import java.util.*

class UpdateScheduleActivity : AppCompatActivity() {

    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_schedule)
        val backButton = findViewById<ImageView>(R.id.back_button)
        val okButton = findViewById<Button>(R.id.update_ok_button)
        val titleText = findViewById<EditText>(R.id.title_text)
        val dateText = findViewById<TextView>(R.id.update_input_date)
        val startTime = findViewById<TextView>(R.id.update_start_time)
        val endTime = findViewById<TextView>(R.id.update_end_time)
        val content = findViewById<EditText>(R.id.content)
        val place = findViewById<Spinner>(R.id.place_select)

        val intent = intent
        var getName = intent.getStringExtra("name").toString()
        var getTitle = intent.getStringExtra("title").toString()
        var getPlace = intent.getStringExtra("place").toString()
        var getDate = intent.getStringExtra("date").toString()
        var getStartTime = intent.getStringExtra("startTime").toString()
        var getEndTime = intent.getStringExtra("endTime").toString()
        var getContent = intent.getStringExtra("content").toString()
        var getId = intent.getStringExtra("id").toString()
        id = getId
        titleText.setText(getTitle)
        dateText.text = getDate
        startTime.text = getStartTime
        endTime.text = getEndTime
        content.setText(getContent)

        var placeSelect : Int = 0

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.list,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        place.adapter = adapter
        // OnItemSelectedListenerの実装
        place.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // 項目が選択された時に呼ばれる
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (parent?.selectedItem) {
                    "オフィス" -> {
                        placeSelect = 0
                    }
                    "在宅" -> {
                        placeSelect = 1
                    }
                    "外出" -> {
                        placeSelect = 2
                    }
                }
            }
            // 基本的には呼ばれないが、何らかの理由で選択されることなく項目が閉じられたら呼ばれる
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        dateText.setOnClickListener {
            showDatePicker()
        }
        startTime.setOnClickListener {
            showStartTimePicker()
        }
        endTime.setOnClickListener {
            showEndTimePicker()
        }
        backButton.setOnClickListener{
            finish()
        }
        okButton.setOnClickListener{
            val titleStr = titleText.text.toString()
            val dateStr = dateText.text.toString()
            val startTimeInput = startTime.text.toString()
            val endTimeInput = endTime.text.toString()
            val contentInput = content.text.toString()
            getAPI(titleStr, dateStr, placeSelect, startTimeInput, endTimeInput, contentInput)
        }
    }
    private fun showDatePicker() {
        val calender = Calendar.getInstance()
        val toYear = calender.get(Calendar.YEAR)
        val toMonth = calender.get(Calendar.MONTH)
        val today = calender.get(Calendar.DAY_OF_MONTH)
        val dateText = findViewById<TextView>(R.id.update_input_date)
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() { view, year, month, dayOfMonth ->
                calender.set(Calendar.YEAR, year)
                calender.set(Calendar.MONTH, month)
                calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)
                dateText.text = sdf.format(calender.time)
            },
            toYear,
            toMonth,
            today
        )
        datePickerDialog.show()
    }
    private fun showStartTimePicker(){
        val startTime = findViewById<TextView>(R.id.update_start_time)
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val timePickerDialog = TimePickerDialog(
            this, TimePickerDialog.OnTimeSetListener() { view, hourOfDay, minute ->
                startTime.text = String.format("%02d:%02d", hourOfDay, minute)
            }, hour, minute, true
        )
        timePickerDialog.show()
    }
    private fun showEndTimePicker(){
        val endTime = findViewById<TextView>(R.id.update_end_time)
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val timePickerDialog = TimePickerDialog(
            this, TimePickerDialog.OnTimeSetListener() { view, hourOfDay, minute ->
                endTime.text = String.format("%02d:%02d", hourOfDay, minute)
            }, hour, minute, true
        )
        timePickerDialog.show()
    }
    fun getAPI(
        title: kotlin.String,
        schedule_date: kotlin.String,
        place: Int,
        start_time: kotlin.String,
        end_time: kotlin.String,
        content: kotlin.String
    ){
        val pref = this.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val token = pref?.getString("token", "")
        val retrofit = Retrofit.Builder()
            .baseUrl(KrononClient.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(KrononService::class.java)
        val schedule = CreateSchedule(title, schedule_date, place, start_time, end_time, content)
        val call = service.updateSchedule(schedule,id,"Bearer $token", "application/json")
        call.enqueue(object : Callback<UpdateScheduleResponse> {
            override fun onResponse(
                call: Call<UpdateScheduleResponse>,
                response: Response<UpdateScheduleResponse>
            ) {
                if (response.code() == 200) {
                    Toast.makeText(applicationContext, "更新が完了したよ", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@UpdateScheduleActivity,MainButtomNavigationActivity::class.java)
                    startActivity(intent)
                } else {
                    val responseError = response.errorBody()
                    //GsonでKotlinクラスに型を変えてもらえる。
                    val exceptionBody = Gson().fromJson(
                        responseError?.string(),
                        CreateUserErrorResponse::class.java
                    )
                    AlertDialog.Builder(this@UpdateScheduleActivity) // FragmentではActivityを取得して生成
                        .setTitle("エラー")
                        .setMessage(exceptionBody.message.toString())
                        .setPositiveButton("OK", { dialog, which ->
                            // TODO:Yesが押された時の挙動
                            val intent = Intent(this@UpdateScheduleActivity,MainButtomNavigationActivity::class.java)
                            startActivity(intent)
                        })
                        .show()
                }
            }

            override fun onFailure(calll: Call<UpdateScheduleResponse>, t: Throwable) {
//                Toast.makeText(this@NewUserCreateActivity, "Fail", Toast.LENGTH_LONG)
                AlertDialog.Builder(this@UpdateScheduleActivity) // FragmentではActivityを取得して生成
                    .setTitle("ネットワークエラー")
                    .setMessage("ネットワークの接続が悪いです")
                    .setPositiveButton("OK", { dialog, which ->
                        val intent = Intent(this@UpdateScheduleActivity,MainButtomNavigationActivity::class.java)
                        startActivity(intent)
                    })
                    .show()
            }

        })
    }


}