package com.example.sgapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

class DetailScheduleActivity : AppCompatActivity() {

    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_schedule)
        val backButton = findViewById<ImageView>(R.id.back_button)
        val editScheduleButton = findViewById<ImageButton>(R.id.update_button)
        val deleteScheduleButton = findViewById<ImageButton>(R.id.delete_button)
        //受け取った変数を入れる
        val intent = intent
        id = intent.getStringExtra("id").toString()
        getScheduleAPI()
        backButton.setOnClickListener {
            finish()
        }
        editScheduleButton.setOnClickListener {
            val intent = Intent(this, UpdateScheduleActivity::class.java)
            startActivity(intent)
        }
        deleteScheduleButton.setOnClickListener {
            deleteScheduleAPI(id)
        }
    }

    fun getScheduleAPI() {
        var nameText = findViewById<TextView>(R.id.account_name_text)
        var titleText = findViewById<TextView>(R.id.title_text)
        var contentText = findViewById<TextView>(R.id.content_text)
        var dateTimeText = findViewById<TextView>(R.id.datetime_text)
        var place = findViewById<TextView>(R.id.place)


        val pref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        var token = pref?.getString("token", "")
        var URL = KrononClient.BaseUrl
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        token = "Bearer $token"
        val service = retrofit.create(KrononService::class.java)
//        val scheduleDate = ScheduleDate(date)
        val call = service.detailSchedules(id, token, "application/json")

        call.enqueue(object : Callback<ScheduleDetailReaponse> {
            override fun onResponse(
                call: Call<ScheduleDetailReaponse>,
                response: Response<ScheduleDetailReaponse>
            ) {
                if (response.code() == 200) {
                    val response = response.body()
                    nameText.text = "名前：" + response?.data?.name
                    titleText.text = response?.data?.title
                    contentText.text = response?.data?.content

                    val showStartTime = response?.data?.start_time
                    val showEndTime = response?.data?.end_time

                    dateTimeText.text =
                        response?.data?.schedule_date + " " + showStartTime?.dropLast(3) + "~" + showEndTime?.dropLast(3)
                    when (response?.data?.place.toString()) {
                        "0" -> {
                            place.text = "オフィス"
                        }
                        "1" -> {
                            place.text = "在宅"
                        }
                        "2" -> {
                            place.text = "外出"
                        }
                    }
                } else {
                    val responseError = response.errorBody()
                    //GsonでKotlinクラスに型を変えてもらえる。
                    val exceptionBody = Gson().fromJson(
                        responseError?.string(),
                        ScheduleDetailErrorReaponse::class.java
                    )
                    AlertDialog.Builder(this@DetailScheduleActivity) // FragmentではActivityを取得して生成
                        .setTitle("エラー")
                        .setMessage(exceptionBody.message.toString())
                        .setPositiveButton("OK", { dialog, which ->
                            // TODO:Yesが押された時の挙動
                        })
                        .show()
                }
            }

            override fun onFailure(calll: Call<ScheduleDetailReaponse>, t: Throwable) {
//                Toast.makeText(this@NewUserCreateActivity, "Fail", Toast.LENGTH_LONG)
                AlertDialog.Builder(this@DetailScheduleActivity) // FragmentではActivityを取得して生成
                    .setTitle("ネットワークエラー")
                    .setMessage("ネットワークの接続が悪いです")
                    .setPositiveButton("OK", { dialog, which ->
                        // TODO:Yesが押された時の挙動
                    })
                    .show()
            }

        })
    }
    fun deleteScheduleAPI(id:String) {
        val pref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        var token = pref?.getString("token", "")
        var URL = KrononClient.BaseUrl
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        token = "Bearer $token"
        val service = retrofit.create(KrononService::class.java)
//        val scheduleDate = ScheduleDate(date)
        val call = service.deleteSchedules(id, token,"application/json")

        call.enqueue(object : Callback<ScheduleDeleteReaponse> {
            override fun onResponse(
                call: Call<ScheduleDeleteReaponse>,
                response: Response<ScheduleDeleteReaponse>
            ) {
                if (response.code() == 200) {
                    val response = response.body()
                    Toast.makeText(applicationContext, "削除に完了したよ", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val responseError = response.errorBody()
                    //GsonでKotlinクラスに型を変えてもらえる。
                    val exceptionBody = Gson().fromJson(
                        responseError?.string(),
                        CreateUserErrorResponse::class.java
                    )
                    AlertDialog.Builder(this@DetailScheduleActivity) // FragmentではActivityを取得して生成
                        .setTitle("エラー")
                        .setMessage(exceptionBody.message.toString())
                        .setPositiveButton("OK", { dialog, which ->
                            // TODO:Yesが押された時の挙動
                        })
                        .show()
                }
            }

            override fun onFailure(calll: Call<ScheduleDeleteReaponse>, t: Throwable) {
//                Toast.makeText(this@NewUserCreateActivity, "Fail", Toast.LENGTH_LONG)
                AlertDialog.Builder(this@DetailScheduleActivity) // FragmentではActivityを取得して生成
                    .setTitle("ネットワークエラー")
                    .setMessage("ネットワークの接続が悪いです")
                    .setPositiveButton("OK", { dialog, which ->
                        // TODO:Yesが押された時の挙動
                    })
                    .show()
            }

        })
    }
}