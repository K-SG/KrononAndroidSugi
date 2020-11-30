package com.example.sgapp

import android.content.Context
import android.view.LayoutInflater
import java.util.*
import kotlin.collections.ArrayList


class DateManager {
    var calendar: Calendar? = null

    constructor() : super() {
        //カレンダーの日時を初期化
        calendar = Calendar.getInstance()
    }


    //当月の要素を取得
    fun getDays(): List<Date>? {
        //現在の状態を保持
        val startDate = calendar?.time
        //GridViewに表示するマスの合計を計算
        val count: Int = getWeeks()!! * 7
        //当月のカレンダーに表示される前月分の日数を計算
        calendar!![Calendar.DATE] = 1
        val dayOfWeek = calendar!![Calendar.DAY_OF_WEEK] - 1
        calendar!!.add(Calendar.DATE, -dayOfWeek)
        val days: MutableList<Date> = ArrayList()
        for (i in 0 until count) {
            days.add(calendar!!.time)
            calendar!!.add(Calendar.DATE, 1)
        }
        //状態を復元
        calendar!!.time = startDate
        return days
    }

    //週数を取得
    fun getWeeks(): Int? {
        return calendar?.getActualMaximum(Calendar.WEEK_OF_MONTH)
    }
    //曜日を取得
    fun getDayOfWeek(date: Date?): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.DAY_OF_WEEK]
    }
    //翌月へ
    fun nextMonth() {
        calendar?.add(Calendar.MONTH, 1)
    }
    //前月へ
    fun prevMonth() {
        calendar?.add(Calendar.MONTH, -1)
    }

}