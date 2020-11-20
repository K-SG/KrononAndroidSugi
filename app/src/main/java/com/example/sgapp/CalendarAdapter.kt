package com.example.sgapp

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList


public abstract class CalendarAdapter : BaseAdapter() {
    private var dateArray: List<Date> = ArrayList()
    private var mContext: Context? = null
    private var mDateManager: DateManager? = null
    private var mLayoutInflater: LayoutInflater? = null

    //カスタムセルを拡張したらここでWigetを定義
    private class ViewHolder {
        var dateText: TextView? = null
    }

    open fun CalendarAdapter(context: Context) {
        mContext = context
        mLayoutInflater = LayoutInflater.from(mContext)
        mDateManager = DateManager()
        dateArray = mDateManager!!.getDays()
    }

    override fun getCount(): Int {
        return dateArray.size
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView: View? = convertView
        val holder: RecyclerView.ViewHolder
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(
                R.layout.calendar_cell,
                null
            )
            holder = RecyclerView.ViewHolder()
            holder.dateText = convertView.findViewById(R.id.dateText)
            convertView.setTag(holder)
        } else {
            holder = convertView.getTag()
        }

        //セルのサイズを指定
        val dp = mContext!!.resources.displayMetrics.density
        val params = AbsListView.LayoutParams(
            parent.width / 7 - dp.toInt(),
            (parent.height - dp.toInt() * mDateManager!!.getWeeks()) / mDateManager!!.getWeeks()
        )
        convertView.setLayoutParams(params)

        //日付のみ表示させる
        val dateFormat = SimpleDateFormat("d", Locale.US)
        holder.dateText.setText(dateFormat.format(dateArray[position]))

        //当月以外のセルをグレーアウト
        if (mDateManager!!.isCurrentMonth(dateArray[position])) {
            convertView.setBackgroundColor(Color.WHITE)
        } else {
            convertView.setBackgroundColor(Color.LTGRAY)
        }

        //日曜日を赤、土曜日を青に
        val colorId: Int
        colorId = when (mDateManager!!.getDayOfWeek(dateArray[position])) {
            1 -> Color.RED
            7 -> Color.BLUE
            else -> Color.BLACK
        }
        holder.dateText.setTextColor(colorId)
        return convertView
    }


}