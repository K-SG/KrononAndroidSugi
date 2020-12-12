package com.example.sgapp.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sgapp.CalendarAdapter
import com.example.sgapp.R
import java.util.*


class CalendarFragment : Fragment() {

    private lateinit var homeViewModel: CalendarViewModel
    var root: View? = null
    lateinit var gridView:GridView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_calendar, container, false)

        setGridView()
        //GridViewのメソッドを呼び出す。
        val adapter = gridView.adapter as CalendarAdapter
        var titleText = root?.findViewById<TextView>(R.id.schedule_date)
        var prevButton = root?.findViewById<TextView>(R.id.prev_month_button)
        var nextButton = root?.findViewById<TextView>(R.id.next_month_button)

        titleText?.text = adapter.getTitle()
        prevButton!!.setOnClickListener {
            //adapterの呼び方が違う
            adapter.prevMonth()
            titleText?.text = adapter.getTitle()
        }
        nextButton!!.setOnClickListener {
            adapter.nextMonth()
            titleText?.text = adapter.getTitle()
        }
        return root
    }

    override fun onStart() {
        super.onStart()
    }

    private fun setGridView() {
        gridView = root?.findViewById<GridView>(R.id.calendarGridView)!!
        gridView?.adapter = CalendarAdapter(context)
    }


}