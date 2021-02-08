package com.example.sgapp.ui.calendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.retrofit2_kotlin.Retrofit2.KrononService
import com.example.sgapp.CalendarAdapter
import com.example.sgapp.CreateScheduleActivity
import com.example.sgapp.R
import com.example.sgapp.api.CalendarErrorResponse
import com.example.sgapp.api.CalendarReaponse
import com.example.sgapp.api.KrononClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CalendarFragment : Fragment() {

    private lateinit var homeViewModel: CalendarViewModel
    var root: View? = null
    lateinit var gridView:GridView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_calendar, container, false)

        setGridView()
        //GridViewのメソッドを呼び出す。
        val adapter = gridView.adapter as CalendarAdapter
        val titleText = root?.findViewById<TextView>(R.id.month_title)
        val prevButton = root?.findViewById<TextView>(R.id.prev_button)
        val nextButton = root?.findViewById<TextView>(R.id.next_button)
        val newScheduleButton = root?.findViewById<ImageView>(R.id.create_button)



        newScheduleButton?.setOnClickListener{
            val intent = Intent(activity,CreateScheduleActivity::class.java)
            startActivity(intent)
        }


        titleText?.text = adapter.getTitle()
        prevButton!!.setOnClickListener {
            //adapterの呼び方が違う
            adapter.prevMonth()
            titleText?.text = adapter.getTitle()
        }
        nextButton!!.setOnClickListener {
            adapter.nextMonth()
            titleText?.text = adapter.getTitle()
        }
        return root
    }

    private fun setGridView() {
        gridView = root?.findViewById<GridView>(R.id.calendar_gridview)!!
        gridView.adapter = CalendarAdapter(context)
    }

    fun getAPI(){
        val pref = context?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        var token = pref?.getString("token", "")
        val retrofit = Retrofit.Builder()
            .baseUrl(KrononClient.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var date = "2021-02-01"
        token = "Bearer $token"
        val service = retrofit.create(KrononService::class.java)
//        val scheduleDate = ScheduleDate(date)
        val call = service.calendar(date,token,"application/json")

        call.enqueue(object : Callback<CalendarReaponse> {
            override fun onResponse(
                call: Call<CalendarReaponse>,
                response: Response<CalendarReaponse>
            ) {
                if (response.code() == 200) {
                    val response = response.body()
                    val responseBody = Gson().fromJson(response.toString(), CalendarReaponse::class.java)
                    var title = responseBody.data?.get(1)?.title
                    println(title)
                } else {
                    val responseError = response.errorBody()
                    val exceptionBody = Gson().fromJson(responseError?.string(), CalendarErrorResponse::class.java)
                    context?.let {
                        AlertDialog.Builder(it) // FragmentではActivityを取得して生成
                            .setTitle("エラー")
                            .setMessage(exceptionBody.message.toString())
                            .setPositiveButton("OK", { dialog, which ->
                                // TODO:Yesが押された時の挙動
                            })
                            .show()
                    }

                }
            }

            override fun onFailure(calll: Call<CalendarReaponse>, t: Throwable) {
//                Toast.makeText(this@NewUserCreateActivity, "Fail", Toast.LENGTH_LONG)
                context?.let {
                    AlertDialog.Builder(it) // FragmentではActivityを取得して生成
                        .setTitle("ネットワークエラー")
                        .setMessage("ネットワークの接続が悪いです")
                        .setPositiveButton("OK", { dialog, which ->
                            // TODO:Yesが押された時の挙動
                        })
                        .show()
                }
            }

        })
    }


}