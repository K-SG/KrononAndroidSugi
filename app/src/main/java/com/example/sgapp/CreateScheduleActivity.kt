package com.example.sgapp

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.retrofit2_kotlin.Retrofit2.KrononService
import com.example.sgapp.api.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class CreateScheduleActivity : AppCompatActivity() {
    var calendar: Calendar? = null
    var dateText : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_schedule)

        var dateText = findViewById<TextView>(R.id.input_date)
        var title = findViewById<EditText>(R.id.title_text)
        var place = findViewById<Spinner>(R.id.place_select)
        var startTime = findViewById<EditText>(R.id.start_time)
        var endTime = findViewById<EditText>(R.id.end_time)
        var content = findViewById<EditText>(R.id.content)
        var placeSelect : Int = 0

        val adapter = ArrayAdapter.createFromResource(this, R.array.list, android.R.layout.simple_spinner_item)

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
                        placeSelect  = 0
                    }
                    "在宅" -> {
                        placeSelect  = 1
                    }
                    "外出" -> {
                        placeSelect  = 2
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
        val okButton = findViewById<Button>(R.id.ok_button)

        okButton.setOnClickListener{
            var titleStr = title.text.toString()
            var dateStr = dateText.text.toString()
            var startTimeInput = startTime.text.toString()
            var endTimeInput = endTime.text.toString()
            var contentInput = content.text.toString()
            getAPI(titleStr, dateStr,placeSelect,startTimeInput, endTimeInput, contentInput)
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
            .baseUrl(KrononClient.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(KrononService::class.java)
        val schedule = CreateSchedule(title, schedule_date, place, start_time, end_time, content)
        val call = service.createSchedule(schedule, "Bearer $token","application/json")
        call.enqueue(object : Callback<CreateScheduleResponse> {
            override fun onResponse(
                call: Call<CreateScheduleResponse>,
                response: Response<CreateScheduleResponse>
            ) {
                if (response.code() == 200) {
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
        val toyear = calender.get(Calendar.YEAR)
        val tomonth = calender.get(Calendar.MONTH)
        val today = calender.get(Calendar.DAY_OF_MONTH)
        var dateText = findViewById<TextView>(R.id.input_date)
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
            toyear,
            tomonth,
            today
        )
        datePickerDialog.show()
    }
}