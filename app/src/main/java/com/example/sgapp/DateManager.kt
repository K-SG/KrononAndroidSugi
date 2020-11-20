package com.example.sgapp

import java.text.SimpleDateFormat
import java.util.*

class DateManager {
    var mCalendar: Calendar? = null

    fun DateManager() {
        mCalendar = Calendar.getInstance()
    }
    //当月の要素を取得
    open fun getDays(): MutableList<Date> {
        //現在の状態を保持
        val startDate = mCalendar!!.time

        //GridViewに表示するマスの合計を計算
        val count: Int = getWeeks() * 7

        //当月のカレンダーに表示される前月分の日数を計算
        mCalendar!![Calendar.DATE] = 1
        val dayOfWeek = mCalendar!![Calendar.DAY_OF_WEEK] - 1
        mCalendar!!.add(Calendar.DATE, -dayOfWeek)
        val days: MutableList<Date> = ArrayList()
        for (i in 0 until count) {
            days.add(mCalendar!!.time)
            mCalendar!!.add(Calendar.DATE, 1)
        }
        //状態を復元
        mCalendar!!.time = startDate
        return days
    }

    //当月かどうか確認
    fun isCurrentMonth(date: Date?): Boolean {
        val format = SimpleDateFormat("yyyy.MM", Locale.US)
        val currentMonth: String = format.format(mCalendar!!.time)
        return if (currentMonth == format.format(date)) {
            true
        } else {
            false
        }
    }

    //週数を取得
    fun getWeeks(): Int {
        return mCalendar!!.getActualMaximum(Calendar.WEEK_OF_MONTH)
    }

    //曜日を取得
    fun getDayOfWeek(date: Date?): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.DAY_OF_WEEK]
    }

    //翌月へ
    fun nextMonth() {
        mCalendar!!.add(Calendar.MONTH, 1)
    }

    //前月へ
    fun prevMonth() {
        mCalendar!!.add(Calendar.MONTH, -1)
    }
}