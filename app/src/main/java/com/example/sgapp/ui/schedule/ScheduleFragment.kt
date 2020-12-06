package com.example.sgapp.ui.schedule

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sgapp.R
import java.util.*

class ScheduleFragment : Fragment() {

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

    companion object {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(ScheduleViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_schedule, container, false)
        mContext = activity

        var dateText = root?.findViewById<TextView>(R.id.schedule_show_date)
        var calendar = Calendar.getInstance()
        var date:java.util.Date = calendar.time
        val dateDisplay: String = DateFormat.format("yyyy年MM月dd日(EEE)の予定", date).toString()
        dateText?.text = dateDisplay

        var blackboardContainer = root?.findViewById<RelativeLayout>(R.id.blackboard)
        blackboardContainer?.removeAllViews()
        showTime(blackboardContainer)
        return root
    }

    private fun showTime(blackboardContainer: RelativeLayout?) {
        mContext = activity
        textView = TextView(mContext)
        textView?.text = "9:00"
        textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, timeFontSize)
        textView?.setTextColor(Color.WHITE)
        val param = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        textView?.layoutParams = param
        blackboardContainer?.addView(textView)
//        for (i in 0 until (endDayTime - startDayTime+1)) {
//            textView?.text = "${i + startDayTime}:00"
//            textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, timeFontSize)
//            val param = RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//            )
//            param.addRule(RelativeLayout.ALIGN_PARENT_TOP)
//            param.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
//            param.setMargins(
//                (timeStartOffsetX * scale).toInt(),
//                (stepY * 4 * i * scale + timeStartOffsetY * scale).toInt(),
//                0,
//                0
//            )
//            textView?.layoutParams = param
//
//            blackboardContainer?.addView(textView)
//        }
    }

}