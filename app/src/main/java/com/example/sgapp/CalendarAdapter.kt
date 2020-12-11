package com.example.sgapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalendarAdapter: BaseAdapter {
    //追加
    private var dateArray: List<Date> = ArrayList()
    private var dateManager: DateManager? = null
    private var layoutInflater: LayoutInflater? = null
    private var calendarDays: List<String> = ArrayList()
    var context: Context? = null

    constructor(context: Context?) : super() {
        this.context = context
        layoutInflater = LayoutInflater.from(context)
        dateManager = DateManager()
        dateArray = dateManager?.getDays()!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val dateFormat = SimpleDateFormat("d", Locale.JAPAN)
        calendarDays = listOf(dateFormat.format(dateArray[position]))
        //日付のみ表示させる
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var calendar: View = inflator.inflate(R.layout.calendar_cell, null)
        calendar.findViewById<TextView>(R.id.dateText).text = dateFormat.format(dateArray[position]).toString()

        //当日の背景を黄色に（今回追記）
        if (dateManager?.isToday()!!.equals(dateArray[position])) {
            convertView?.setBackgroundColor(Color.YELLOW);
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
    fun getTitle(): String? {
        val format = SimpleDateFormat("yyyy年M月", Locale.JAPAN)
        return format.format(dateManager?.calendar?.time)
    }


    //翌月表示
    fun nextMonth() {
        dateManager?.nextMonth()
        dateArray = dateManager?.getDays()!!
        notifyDataSetChanged()
    }

    //前月表示
    fun prevMonth() {
        dateManager?.prevMonth()
        dateArray = dateManager?.getDays()!!
        //画面をリフレッシュしてくれる
        notifyDataSetChanged()
    }
}
