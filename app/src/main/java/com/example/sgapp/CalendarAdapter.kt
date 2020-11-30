package com.example.sgapp

import android.content.Context
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
        val dateFormat = SimpleDateFormat("dd", Locale.JAPAN)
        calendarDays = listOf(dateFormat.format(dateArray[position]))
        //日付のみ表示させる
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var calendar: View = inflator.inflate(R.layout.calendar_cell, null)
        calendar.findViewById<TextView>(R.id.dateText).text = dateFormat.format(dateArray[position]).toString()
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
        val format = SimpleDateFormat("yyyy.MM", Locale.US)
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
