package com.example.sgapp.ui.schedule

import android.content.Context
import android.content.res.Resources
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
    private var textView: TextView? = null

    companion object {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(ScheduleViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_schedule, container, false)
        var dateText = root?.findViewById<TextView>(R.id.schedule_show_date)
        var calendar = Calendar.getInstance()
        var date:java.util.Date = calendar.time
        val dateDisplay: String = DateFormat.format("yyyy年MM月dd日(EEE)の予定", date).toString()
        dateText?.text = dateDisplay

        var blackboardContainer = root?.findViewById<RelativeLayout>(R.id.blackboard)
        showTime(blackboardContainer)
        return root
    }

    private fun showTime(blackboardContainer: RelativeLayout?) {
        textView = TextView(mContext)
        textView?.text = "9:00"
        textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)

        val param = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        textView?.layoutParams = param
        blackboardContainer?.addView(textView)
    }
//            textView.setTextColor(Color.WHITE)
//            textView.text = "${i + startDayTime}:00"
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,timeFontSize)
//            val param = RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//                )
//            textView.layoutParams = param
//            blackboardContainer?.addView(textView)
}