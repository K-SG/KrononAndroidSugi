package com.example.sgapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.retrofit2_kotlin.Retrofit2.KrononService
import com.example.sgapp.api.*
import com.example.sgapp.api.KrononClient.Companion.BaseUrl
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.String.format
import java.text.SimpleDateFormat
import java.util.*


class CreateScheduleActivity : AppCompatActivity() {
    var context: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_schedule)

        val dateText = findViewById<TextView>(R.id.input_date)
        val title = findViewById<EditText>(R.id.title_text)
        val place = findViewById<Spinner>(R.id.place_select)
        val startTime = findViewById<TextView>(R.id.new_start_time)
        val endTime = findViewById<TextView>(R.id.new_end_time)
        val content = findViewById<EditText>(R.id.content)
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
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener{
            finish()
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
        val okButton = findViewById<Button>(R.id.ok_button)

        okButton.setOnClickListener{
            val titleStr = title.text.toString()
            val dateStr = dateText.text.toString()
            val startTimeInput = startTime.text.toString()
            val endTimeInput = endTime.text.toString()
            val contentInput = content.text.toString()
            getAPI(titleStr, dateStr, placeSelect, startTimeInput, endTimeInput, contentInput)
        }

    }
    fun getAPI(
        title: String,
        schedule_date: String,
        place: Int,
        start_time: String,
        end_time: String,
        content: String
    ){
        val pref = this.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val token = pref?.getString("token", "")
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(KrononService::class.java)
        val schedule = CreateSchedule(title, schedule_date, place, start_time, end_time, content)
        val call = service.createSchedule(schedule, "Bearer $token", "application/json")
        call.enqueue(object : Callback<CreateScheduleResponse> {
            override fun onResponse(
                call: Call<CreateScheduleResponse>,
                response: Response<CreateScheduleResponse>
            ) {
                if (response.code() == 200) {
                    Toast.makeText(applicationContext, "登録に完了したよ", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val responseError = response.errorBody()
                    //GsonでKotlinクラスに型を変えてもらえる。
                    val exceptionBody = Gson().fromJson(
                        responseError?.string(),
                        CreateUserErrorResponse::class.java
                    )
                    AlertDialog.Builder(this@CreateScheduleActivity) // FragmentではActivityを取得して生成
                        .setTitle("エラー")
                        .setMessage(exceptionBody.message.toString())
                        .setPositiveButton("OK", { dialog, which ->
                            // TODO:Yesが押された時の挙動
                        })
                        .show()
                }
            }

            override fun onFailure(calll: Call<CreateScheduleResponse>, t: Throwable) {
//                Toast.makeText(this@NewUserCreateActivity, "Fail", Toast.LENGTH_LONG)
                AlertDialog.Builder(this@CreateScheduleActivity) // FragmentではActivityを取得して生成
                    .setTitle("ネットワークエラー")
                    .setMessage("ネットワークの接続が悪いです")
                    .setPositiveButton("OK", { dialog, which ->
                        // TODO:Yesが押された時の挙動
                    })
                    .show()
            }

        })
    }
    private fun showDatePicker() {
        val calender = Calendar.getInstance()
        val toYear = calender.get(Calendar.YEAR)
        val toMonth = calender.get(Calendar.MONTH)
        val today = calender.get(Calendar.DAY_OF_MONTH)
        val dateText = findViewById<TextView>(R.id.input_date)
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
        val startTime = findViewById<TextView>(R.id.new_start_time)
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val timePickerDialog = TimePickerDialog(
            this, TimePickerDialog.OnTimeSetListener() { view, hourOfDay, minute ->
                startTime.text = format("%02d:%02d", hourOfDay, minute)
            }, hour, minute, true
        )
        timePickerDialog.show()
    }
    private fun showEndTimePicker(){
        val endTime = findViewById<TextView>(R.id.new_end_time)
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]

        val timePickerDialog = TimePickerDialog(
            this, TimePickerDialog.OnTimeSetListener() { view, hourOfDay, minute ->
                endTime.text = format("%02d:%02d", hourOfDay, minute)
            }, hour, minute, true
        )
        timePickerDialog.show()
    }

}