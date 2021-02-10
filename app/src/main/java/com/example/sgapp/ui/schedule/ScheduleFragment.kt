package com.example.sgapp.ui.schedule

import android.content.Context
import android.content.Intent
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
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.retrofit2_kotlin.Retrofit2.KrononService
import com.example.sgapp.*
import com.example.sgapp.api.*
import com.example.sgapp.api.KrononClient.Companion.BaseUrl
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class ScheduleFragment : Fragment() {

    private lateinit var dashboardViewModel: ScheduleViewModel
    private var root: View? = null
    private var timeFontSize = 10f
    private val startHour = 8
    private val endHour = 20
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
//    var names : Array<String> = arrayOf()
    private var names = arrayOf("", "", "")
    private val nx = 3//names.size
    var calendar: Calendar = Calendar.getInstance()
    private var scheduleArray :
            Array<Array<ScheduleShortData?>?>?= null
    companion object {
    }
    var dateDisplay =""
    val format = SimpleDateFormat("yyyy-MM-dd")
    val today = Date()
    var dateAPI = format.format(today)
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
        dateDisplay = DateFormat.format("yyyy年MM月dd日(EEE)の予定", today).toString()
        if(arguments != null){
            requireArguments().run {
                dateAPI = getString("date").toString()
                dateDisplay = getString("dateDisplay").toString()
            }
        }

        var scheduledate = format.parse(dateAPI)
        calendar.time = scheduledate
        val date: Calendar = calendar
        //コンテキストを取得
        mContext = activity
        val dateText = root?.findViewById<TextView>(R.id.schedule_show_date)
        val prevButton = root?.findViewById<TextView>(R.id.prev_day_button)
        val nextButton = root?.findViewById<TextView>(R.id.next_day_button)
        val newScheduleButton = root?.findViewById<ImageView>(R.id.create_button)
        //日付フォーマット
        dateText?.text = dateDisplay
//        val format = SimpleDateFormazt("yyyy-MM-dd")
//        val today = Date()
//        var dateAPI = format.format(today)
//        //Date型のも用意
//        val scheduledate = format.parse(dateAPI)

        newScheduleButton?.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, CreateScheduleActivity::class.java)
            startActivity(intent)
        })

        prevButton!!.setOnClickListener{
            //adapterの呼び方が違う
            date.add(Calendar.DATE, -1)
            dateDisplay = DateFormat.format("yyyy年MM月dd日(EEE)の予定", date).toString()
            var preDate = DateFormat.format("yyyy-MM-dd", date).toString()
            scheduledate = format.parse(preDate)
            dateText?.text = dateDisplay
            val params = bundleOf("date" to preDate,"dateDisplay" to dateDisplay)
            findNavController()?.navigate(
                R.id.navigation_schedule,params
            )
        }
        nextButton!!.setOnClickListener {
            date.add(Calendar.DATE, 1)
            dateDisplay = DateFormat.format("yyyy年MM月dd日(EEE)の予定", date).toString()
            var preDate = DateFormat.format("yyyy-MM-dd", date).toString()
            scheduledate = format.parse(preDate)
            dateText?.text = dateDisplay
            val params = bundleOf("date" to preDate,"dateDisplay" to dateDisplay)
            findNavController()?.navigate(
                R.id.navigation_schedule,params
            )
        }
        getAPI(root!!,scheduledate)

        //端末の幅のサイズ[pixel]
        widthPixel = resources.displayMetrics.widthPixels
        Log.i("Tag", "width:$widthPixel")
        // dp 設定
        scale = resources.displayMetrics.density

        val blackboardContainer = root?.findViewById<RelativeLayout>(R.id.blackboard)
        blackboardContainer?.removeAllViews()
        return root
    }
    fun getAPI(root: View,date: Date){
        val pref = activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        var token = pref?.getString("token", "")


        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val format = SimpleDateFormat("yyyy-MM-dd")
        var dateAPI = format.format(date)
        token = "Bearer $token"
        val service = retrofit.create(KrononService::class.java)
//        val scheduleDate = ScheduleDate(date)
        val call = service.showSchedules(dateAPI,token,"application/json")

        call.enqueue(object : Callback<ShowScheduleResponse> {
            override fun onResponse(
                call: Call<ShowScheduleResponse>,
                response: Response<ShowScheduleResponse>
            ) {
                if (response.code() == 200) {
                    var responseBody = response.body()
                    names = responseBody?.getNamesArray()!!
                    scheduleArray = responseBody.getScheduleShortArray()
                    showSchedules(root,date)
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

    private fun showSchedules(root: View, date:Date){
        val dateTextView = root.findViewById<TextView>(R.id.update_date)
        val dateDisplay: String = DateFormat.format("yyyy年MM月dd日(EEE)の予定", date).toString()
        dateTextView?.text = dateDisplay

        //端末の幅のサイズ[pixel]
        widthPixel = resources.displayMetrics.widthPixels
        Log.i("Tag", "width:$widthPixel")

        // dp 設定
        scale = resources.displayMetrics.density

        //予定ブロックの幅
        widthContent = ((widthPixel - lineReduceLength * scale) / nx).toInt()

        //配置するレイアウトの取得
        val blackboardContainer = root.findViewById<RelativeLayout>(R.id.blackboard)


        //名前表示
        viewName(blackboardContainer)

        //線描画
        drawLine(R.drawable.stlike, 0, endHour - startHour + 1, blackboardContainer)


        //点線描画
        drawLine(R.drawable.dotted, 2, endHour - startHour + 1, blackboardContainer)


        //時間表示
        viewTime(blackboardContainer)

        //予定表示
        viewSchedule(blackboardContainer)
    }

    //名前表示
    private fun viewName(layout: RelativeLayout){
        for (i in 0 until nx) {
            val textView = TextView(activity)
            textView.text = names[i]

            // テキストサイズ 30sp
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, timeFontSize)


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

    private fun drawLine(pattern: Int, offset: Int, number : Int,layout: RelativeLayout){
        for (i in 0 until (number)) {
            val imageView = ImageView(activity)

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

    private fun viewSchedule(layout: RelativeLayout) {
        for ( i in  scheduleArray!!.indices) {

            val schedules = scheduleArray!![i]

            for(schedule in schedules!!) {
                val contentHeight = (schedule!!.endTime - schedule!!.startTime) / 15
                val contentStart = (schedule!!.startTime - startHour * 60) / 15
                val contentColor = schedule!!.place

                val textView = TextView(activity)
                textView.text = schedule.title

                // テキストサイズ 30sp
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, timeFontSize)
                activity?.let { ContextCompat.getColor(it, R.color.common_text) }?.let {
                    textView.setTextColor(
                        it
                    )
                }


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
                    (widthContent * i + lineStartOffsetX * scale + contentMargin).toInt(),
                    (stepY * contentStart * scale + lineStartOffsetY * scale).toInt(),
                    contentMargin.toInt(),
                    0
                )

                textView.layoutParams = param

                textView.gravity = Gravity.CENTER

                layout.addView(textView)

                textView.setOnClickListener(View.OnClickListener {
                    var detailDate = schedule.scheduleId
                    var intent = Intent(activity, DetailScheduleActivity::class.java)
                    intent.putExtra("id",detailDate.toString())
                    startActivity(intent)
                })
            }
        }
    }
    //時間表示
    private fun viewTime(layout: RelativeLayout){

        for (i in 0 until (endHour - startHour + 1)) {
            val textView = TextView(activity)
            textView.text = "${i + startHour}:00"

            // テキストサイズ 30sp
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, timeFontSize)


            val param =
                RelativeLayout.LayoutParams(
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

            textView.layoutParams = param

            layout.addView(textView)


        }
    }



}