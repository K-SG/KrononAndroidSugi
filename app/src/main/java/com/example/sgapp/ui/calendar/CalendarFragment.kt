package com.example.sgapp.ui.calendar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sgapp.CalendarAdapter
import com.example.sgapp.R
import java.security.AccessController.getContext
import java.util.ArrayList

class CalendarFragment : Fragment() {

//    private var context = null
    private lateinit var homeViewModel: CalendarViewModel
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
