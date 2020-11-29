package com.example.sgapp.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sgapp.CalendarAdapter
import com.example.sgapp.DateManager
import com.example.sgapp.R
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment() {

//    private var context = null
    private lateinit var homeViewModel: CalendarViewModel
    private var calendarAdapter: CalendarAdapter? = null
    private var dateManager:DateManager? = null
    var calendar: Calendar? = null
    var root: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        context = getContext() as Nothing?
//        context = getContext()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_calendar, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        var titleText = root?.findViewById<TextView>(R.id.titleText)
        var prevButton = root?.findViewById<Button>(R.id.prevButton)
        var nextButton = root?.findViewById<Button>(R.id.nextButton)

        prevButton!!.setOnClickListener {
            calendarAdapter?.prevMonth()
            titleText?.text = getMonthTitle()
        }
        nextButton!!.setOnClickListener {
            calendarAdapter?.nextMonth()
            titleText?.text = getMonthTitle()
        }
//        var test = calendaradapter?.getTitle()
//        var test2 = getMonth()
//        titleText?.text = calendaradapter?.getTitle()
//        var titleFormat = SimpleDateFormat("yyyy.MM", Locale.US)
//        var titleMonth = mDateManager?.mCalendar?.tim
        titleText?.text = getMonthTitle()
        return root
    }

    override fun onStart() {
        super.onStart()
        setGridView();
    }

    private fun setGridView() {
//        val gridView = GridView(context)
        val gridView = root?.findViewById<GridView>(R.id.calendarGridView)
//        gridView?.adapter = activity?.applicationContext?.let { ArrayAdapter(it,android.R.layout.simple_list_item_1,texts) }
        gridView?.adapter = CalendarAdapter(context)

    }

    private fun getMonthTitle(): String {
        var titleFormat = SimpleDateFormat("yyyy.MM", Locale.US)
        var month = Date()
        return titleFormat.format(month)
    }


}
//class CalendarAdapter:BaseAdapter{
//    private val texts = arrayOf(
//        1, 2, 3, 4, 5,
//        6, 7, 8
//    )
//    var context: Context? = null
//
//    constructor(context: Context?) : super() {
//        this.context = context
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
//        //calendar
//        val calendar_number = texts[position]
//
//        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        var calendar:View = inflator.inflate(R.layout.calendar_cell, null)
////        foodView.imgFood.setImageResource(food.image!!)
//
//        calendar.findViewById<TextView>(R.id.dateText).text = calendar_number.toString()
//
//        return calendar
//    }
//
//    override fun getCount(): Int {
//        return texts.size
//    }
//
//    override fun getItem(position: Int): Any {
//        return texts[position]
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//}
