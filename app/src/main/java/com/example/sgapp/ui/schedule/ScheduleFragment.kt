package com.example.sgapp.ui.schedule

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.retrofit2_kotlin.Retrofit2.KrononService
import com.example.sgapp.*
import com.example.sgapp.api.*
import com.example.sgapp.api.KrononClient.Companion.BaseUrl
import com.example.sgapp.ui.login.LoginActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class ScheduleFragment : Fragment() {

    private var dateManager: DateManager? = null
    private lateinit var dashboardViewModel: ScheduleViewModel
    private var root: View? = null
    private var timeFontSize = 10f
    private var startDayTime = 8
    private var endDayTime = 20
    private var mContext :Context? = null
    private var textView :TextView? = null
    private val stepY = 20f
    private val timeStartOffsetX = 5f
    private val timeStartOffsetY = 10f
    private val lineStartOffsetX = 35f
    private val lineStartOffsetY = 25f
    private val nameStartOffsetX = 30f
    private val nameStartOffsetY = 0f
    private val lineReduceLength = 80f
    private val contentMargin = 15f
    private var widthPixel = 0
    private var widthContent = 0
    private var scale = 0f
    var names : Array<String> = arrayOf()
    private val nx = names.size
    var calendar: Calendar = Calendar.getInstance()
    val schedules = arrayOf(
        ScheduleModel(1, 1, "2020/12/20","タスク１" ,10 * 60, 11 * 60 + 30, 0),
        ScheduleModel(0, 2, "2020/12/20","タスク１" ,15 * 60, 16 * 60 + 30, 2)
    )
//    val schedules = arrayOf<String>()

    companion object {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var calendar = Calendar.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(ScheduleViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_schedule, container, false)
        //コンテキストを取得
        mContext = activity
        val dateText = root?.findViewById<TextView>(R.id.schedule_show_date)
        val prevButton = root?.findViewById<TextView>(R.id.prev_day_button)
        val nextButton = root?.findViewById<TextView>(R.id.next_day_button)
        val newScheduleButton = root?.findViewById<ImageView>(R.id.create_button)
        val date: Calendar = calendar
        //日付フォーマット
        var dateDisplay: String = DateFormat.format("yyyy年MM月dd日(EEE)の予定", date).toString()
        dateText?.text = dateDisplay
        names = arrayOf("中根", "奥野", "杉")
        getAPI()

        newScheduleButton?.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, CreateScheduleActivity::class.java)
            startActivity(intent)
        })

        prevButton!!.setOnClickListener {
            //adapterの呼び方が違う
            date.add(Calendar.DATE, -1)
            dateDisplay = DateFormat.format("yyyy年MM月dd日(EEE)の予定", date).toString()
            dateText?.text = dateDisplay
        }
        nextButton!!.setOnClickListener {
            date.add(Calendar.DATE, 1)
            dateDisplay = DateFormat.format("yyyy年MM月dd日(EEE)の予定", date).toString()
            dateText?.text = dateDisplay
        }

        //端末の幅のサイズ[pixel]
        widthPixel = resources.displayMetrics.widthPixels
        Log.i("Tag", "width:$widthPixel")
        // dp 設定
        scale = resources.displayMetrics.density

        val blackboardContainer = root?.findViewById<RelativeLayout>(R.id.blackboard)
        blackboardContainer?.removeAllViews()
        //予定ブロックの幅
        widthContent = ((widthPixel - lineReduceLength * scale) / nx).toInt()

        blackboardContainer?.let { viewName(it) }

        //線描画
        blackboardContainer?.let { drawLine(R.drawable.stlike, 0, it) }


        //点線描画
        blackboardContainer?.let { drawLine(R.drawable.dotted, 2, it) }

        showTime(blackboardContainer)

        //予定表示
        blackboardContainer?.let { viewSchedule(it) }
        return root
    }

    //名前表示
    @SuppressLint("ResourceAsColor")
    private fun viewName(layout: RelativeLayout){
        mContext = activity
        
        val nx = names.size
        for (i in 0 until nx) {
            val textView = TextView(mContext)
            textView.text = names[i]

            // テキストサイズ 30sp
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, timeFontSize)
            textView?.setTextColor(Color.WHITE)


            val param = RelativeLayout.LayoutParams(
                widthContent,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            param.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            param.addRule(RelativeLayout.ALIGN_PARENT_LEFT)


            param.setMargins(
                (widthContent * i + nameStartOffsetX * scale).toInt(),
                (nameStartOffsetY * scale).toInt(),
                0,
                0
            )

            textView.layoutParams = param

            textView.gravity = Gravity.CENTER

            layout.addView(textView)
        }
    }

    private fun drawLine(pattern: Int, offset: Int, layout: RelativeLayout){
        mContext = activity
        for (i in 0 until (endDayTime - startDayTime)) {
            val imageView = ImageView(mContext)

            imageView.setImageResource(pattern)

            val param =
                RelativeLayout.LayoutParams(
                    (widthPixel - lineReduceLength * scale).toInt(),
                    (2 * scale).toInt()
                )

            param.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            param.addRule(RelativeLayout.ALIGN_PARENT_LEFT)

            param.setMargins(
                (lineStartOffsetX * scale).toInt(),
                (stepY * (4 * i + offset) * scale + lineStartOffsetY * scale).toInt(),
                0,
                0
            )

            imageView.layoutParams = param

            layout.addView(imageView)
        }
    }

    private fun showTime(blackboardContainer: RelativeLayout?) {
        mContext = activity
        for (i in 0 until (endDayTime - startDayTime+1)) {
            textView = TextView(mContext)
            textView?.text = "${i + startDayTime}:00"
            textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, timeFontSize)
            textView?.setTextColor(Color.WHITE)

            val param = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            param.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            param.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            param.setMargins(
                (timeStartOffsetX * scale).toInt(),
                (stepY * 4 * i * scale + timeStartOffsetY * scale).toInt(),
                0,
                0
            )
            textView?.layoutParams = param

            blackboardContainer?.addView(textView)
        }
    }
    private fun viewSchedule(layout: RelativeLayout) {
        mContext = activity
        for (schedule in schedules) {
            val contentHeight = (schedule.endTime - schedule.startTime) / 15
            val contentStart = (schedule.startTime - startDayTime * 60) / 15
            val contentColor = schedule.place

            val textView = TextView(mContext)
            textView.text = schedule.title

            // テキストサイズ 30sp
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, timeFontSize)
            textView.setTextColor(Color.GRAY)


            when (contentColor) {
                0 -> textView.setBackgroundResource(R.color.place_home)
                1 -> textView.setBackgroundResource(R.color.place_office)
                2 -> textView.setBackgroundResource(R.color.place_away)

            }

            val param =
                RelativeLayout.LayoutParams(
                    (widthContent - contentMargin * 2).toInt(),
                    (stepY * scale * contentHeight).toInt()
                )

            param.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            param.addRule(RelativeLayout.ALIGN_PARENT_LEFT)


            param.setMargins(
                (widthContent * schedule.userId + lineStartOffsetX * scale + contentMargin).toInt(),
                (stepY * contentStart * scale + lineStartOffsetY * scale).toInt(),
                contentMargin.toInt(),
                0
            )
            textView.setOnClickListener(View.OnClickListener {
                var intent = Intent(activity,DetailScheduleActivity::class.java)
                startActivity(intent)
            })

            textView.layoutParams = param

            textView.gravity = Gravity.CENTER

            layout.addView(textView)

        }
    }
    fun getAPI(){
        val pref = activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        var token = pref?.getString("token", "")
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var date = "2021-01-01"
        token = "Bearer $token"
        val service = retrofit.create(KrononService::class.java)
//        val scheduleDate = ScheduleDate(date)
        val call = service.showSchedules(date,token)

        call.enqueue(object : Callback<ShowScheduleResponse> {
            override fun onResponse(
                call: Call<ShowScheduleResponse>,
                response: Response<ShowScheduleResponse>
            ) {
                if (response.code() == 200) {
                    names = arrayOf("中根", "奥野", "杉")
                } else {
                    val responseError = response.errorBody()
                    val exceptionBody = Gson().fromJson(responseError?.string(), LoginUserErrorResponse::class.java)
                    activity?.let {
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

            override fun onFailure(calll: Call<ShowScheduleResponse>, t: Throwable) {
//                Toast.makeText(this@NewUserCreateActivity, "Fail", Toast.LENGTH_LONG)
                activity?.let {
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