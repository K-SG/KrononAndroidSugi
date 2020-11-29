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

    private val texts = arrayOf(
        1, 2, 3, 4, 5,
        6, 7, 8, 9, 10
    )
    var context: Context? = null

    constructor(context: Context?) : super() {
        this.context = context
        //追加
        layoutInflater = LayoutInflater.from(context)
        dateManager = DateManager()
        dateArray = dateManager?.getDays()!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val dateFormat = SimpleDateFormat("dd", Locale.JAPAN)
        //calendar
//        val calendar_number = texts[position]
//        //追加
//        var calendar_days = dateArray[position]
//        val format = dateFormat.format(dateArray[position])
        calendarDays = listOf(dateFormat.format(dateArray[position]))
        //日付のみ表示させる
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var calendar: View = inflator.inflate(R.layout.calendar_cell, null)
//        foodView.imgFood.setImageResource(food.image!!)
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
        notifyDataSetChanged()
    }




}
