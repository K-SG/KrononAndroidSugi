package com.example.sgapp

import android.R
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalendarAdapter(context: Context) : BaseAdapter() {
    private var dateArray: List<Date> = ArrayList()
    private val mContext: Context
    private val mDateManager: DateManager
    private val mLayoutInflater: LayoutInflater

    //カスタムセルを拡張したらここでWigetを定義
    private class ViewHolder {
        var dateText: TextView? = null
    }

    override fun getCount(): Int {
        return dateArray.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView: View? = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.,null)
            holder = ViewHolder()
            holder.dateText = convertView?.findViewById()
            convertView?.setTag(holder)
        } else {
            holder = convertView.getTag() as ViewHolder
        }

        //セルのサイズを指定
        val dp: Float = mContext.getResources().getDisplayMetrics().density
        val params = AbsListView.LayoutParams(
            parent.width / 7 - dp.toInt(),
            (parent.height - dp.toInt() * mDateManager.getWeeks()) / mDateManager.getWeeks()
        )
        convertView?.setLayoutParams(params)

        //日付のみ表示させる
        val dateFormat = SimpleDateFormat("d", Locale.US)
        holder.dateText?.setText(dateFormat.format(dateArray[position]))

        //当月以外のセルをグレーアウト
        if (mDateManager.isCurrentMonth(dateArray[position])) {
            convertView?.setBackgroundColor(Color.WHITE)
        } else {
            convertView?.setBackgroundColor(Color.LTGRAY)
        }

        //日曜日を赤、土曜日を青に
        val colorId: Int
        colorId = when (mDateManager.getDayOfWeek(dateArray[position])) {
            1 -> Color.RED
            7 -> Color.BLUE
            else -> Color.BLACK
        }
        holder.dateText!!.setTextColor(colorId)
        return convertView
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    //表示月を取得
    val title: String
        get() {
            val format = SimpleDateFormat("yyyy.MM", Locale.US)
            return format.format(mDateManager.mCalendar!!.time)
        }

    //翌月表示
    fun nextMonth() {
        mDateManager.nextMonth()
        dateArray = mDateManager.getDays()
        notifyDataSetChanged()
    }

    //前月表示
    fun prevMonth() {
        mDateManager.prevMonth()
        dateArray = mDateManager.getDays()
        notifyDataSetChanged()
    }

    init {
        mContext = context
        mLayoutInflater = LayoutInflater.from(mContext)
        mDateManager = DateManager()
        dateArray = mDateManager.getDays()
    }
}