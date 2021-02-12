package com.example.sgapp

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
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
import kotlin.collections.ArrayList


class CalendarAdapter : BaseAdapter {
    //追加
    private var dateArray: List<Date> = ArrayList()
    private var dateManager: DateManager? = null
    private var layoutInflater: LayoutInflater? = null
    private var calendarDays: List<String> = ArrayList()
    var context: Context? = null
    var position:Int = 0
    val format = SimpleDateFormat("yyyy-MM-dd")
    val today = Date()
    var toMonth = format.format(today)

    //カスタムセルを拡張したらここでWigetを定義
    private class ViewHolder {
        var dateText: TextView? = null
    }

    private var oldSchedules : MutableList<CalendarSchedule> = mutableListOf()
//    (
////        CalendarData("2021/02/08", "12:00", "しんどおお"),
////        CalendarData("2021/02/09", "12:00", "しんどおお")
//    )

    //
//    private val scheduless :Array<CalendarData?>? = arrayOf(
//        ScheduleModel(1, 1, "2021/01/01", "タスク１", 13, 14, 3),
//        ScheduleModel(1, 2, "2021/01/01", "タスク2あああああああs", 15, 16, 3)
//    )

    private var schedules: Array<CalendarSchedule?>? = null

    constructor(context: Context?) : super() {
        this.context = context
        layoutInflater = LayoutInflater.from(context)
        dateManager = DateManager()
        dateArray = dateManager?.getDays()!!
        getAPI()

    }




    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //日付のみ表示させる
        val inflator = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val calendar: View = inflator.inflate(R.layout.calendar_cell, null)
        val dateFormat = SimpleDateFormat("d", Locale.JAPAN)
        calendarDays = listOf(dateFormat.format(dateArray[position]))
        val dateFormat_date = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
        var titletext = calendar.findViewById<TextView>(R.id.title1)
        calendar.findViewById<TextView>(R.id.dateText).text =
            dateFormat.format(dateArray[position]).toString()

        calendar.setBackgroundColor(Color.WHITE)
        //日付クラスのリストからその日付のインスタンスを呼び出してVIEWを作る、カレンダークラスの中に日付のクラス（中にイベントクラスがある）のリストを持たせる

//
//        getAPI()
        for (schedule in oldSchedules) {
            if (dateFormat_date.format(dateArray[position]).toString() == schedule?.date) {
                titletext.text = schedule.title
                titletext.setBackgroundResource(R.drawable.event)
            }
        }

        var holder: RecyclerView.ViewHolder


        //カレンダータップすると予定表に飛びたいんだが
//        calendar.setOnClickListener(){
//            convertView?.findNavController()?.navigate(
//                R.id.navigation_schedule
//            )
//        }
        if (dateManager?.getDayOfWeek(dateArray[position]) == 1) {
            calendar.setBackgroundResource(R.color.sunday_color)
        }
        if (dateManager?.getDayOfWeek(dateArray[position]) == 7) {
            calendar.setBackgroundResource(R.color.saturday_color)
        }

        //当日の背景に色をつける
        if (dateManager?.isToday(dateArray[position]) == true) {
            calendar.setBackgroundResource(R.color.today);
        }
        return calendar
    }

    override fun getCount(): Int {
        return dateArray.size
    }

    override fun getItem(position: Int): Any {
        return dateArray[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //表示月を取得
    fun getTitle(): String {
        val format = SimpleDateFormat("yyyy年M月", Locale.JAPAN)
        return format.format(dateManager?.calendar?.time)
        notifyDataSetChanged()
    }


    //翌月表示
    fun nextMonth() {
        dateManager?.nextMonth()
        dateArray = dateManager?.getDays()!!
        //画面をリフレッシュしてくれる
        notifyDataSetChanged()
    }

    //前月表示
    fun prevMonth() {
        dateManager?.prevMonth()
        dateArray = dateManager?.getDays()!!
        //画面をリフレッシュしてくれる
        notifyDataSetChanged()
    }

    fun getAPI() {
        val pref = context?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        var token = pref?.getString("token", "")
        val retrofit = Retrofit.Builder()
            .baseUrl(KrononClient.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var date = toMonth

        token = "Bearer $token"
        val service = retrofit.create(KrononService::class.java)
//        val scheduleDate = ScheduleDate(date)
        val call = service.calendar(date, token, "application/json")

        call.enqueue(object : Callback<CalendarReaponse> {
            override fun onResponse(
                call: Call<CalendarReaponse>,
                response: Response<CalendarReaponse>
            ) {
                if (response.code() == 200) {
                    val response = response.body()
//                    response?.getCalendarArray()
                    var resData = response?.data
                    for (i in resData?.indices!!) {
                        oldSchedules.add(response?.data?.get(i)!!)
                    }
                    val inflator = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val dateFormat_date = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
                    val calendar: View = inflator.inflate(R.layout.calendar_cell, null)
                    notifyDataSetChanged()
//                    for (schedule in oldSchedules) {
//                        if (todays == schedule?.date) {
//                            titletext.text = schedule.title
//                            titletext.setBackgroundResource(R.drawable.event)
//                        }
//                    }
//                    schedules = response?.getCalendarArray()
//                    Log.i("nameArray",schedules.toString())
                } else {
                    val responseError = response.errorBody()
                    val exceptionBody =
                        Gson().fromJson(responseError?.string(), CalendarErrorResponse::class.java)
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
