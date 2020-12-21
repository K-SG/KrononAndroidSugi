package com.example.sgapp.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sgapp.CalendarAdapter
import com.example.sgapp.CreateScheduleActivity
import com.example.sgapp.R


class CalendarFragment : Fragment() {

    private lateinit var homeViewModel: CalendarViewModel
    var root: View? = null
    lateinit var gridView:GridView




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
        val titleText = root?.findViewById<TextView>(R.id.month_title)
        val prevButton = root?.findViewById<TextView>(R.id.prev_button)
        val nextButton = root?.findViewById<TextView>(R.id.next_button)
        val newScheduleButton = root?.findViewById<ImageView>(R.id.create_button)


        newScheduleButton?.setOnClickListener{
            val intent = Intent(activity,CreateScheduleActivity::class.java)
            startActivity(intent)
        }


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

    private fun setGridView() {
        gridView = root?.findViewById<GridView>(R.id.calendar_gridview)!!
        gridView.adapter = CalendarAdapter(context)
    }


}