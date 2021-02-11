package com.example.sgapp.ui.calendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.retrofit2_kotlin.Retrofit2.KrononService
import com.example.sgapp.CalendarAdapter
import com.example.sgapp.CreateScheduleActivity
import com.example.sgapp.R
import com.example.sgapp.api.CalendarErrorResponse
import com.example.sgapp.api.CalendarReaponse
import com.example.sgapp.api.KrononClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment() {

    private lateinit var homeViewModel: CalendarViewModel
    var root: View? = null
    lateinit var gridView:GridView
//    var context: Context? = null


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

//        gridView.setOnItemClickListener(activity)
        adapter.getAPI()


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
        gridView.setOnItemClickListener { parent, view, position, id ->
            Log.d("position", parent.toString())
            Log.d("position", view.toString())
            Log.d("position", position.toString())
            Log.d("position", id.toString())
            Log.d("position", gridView.adapter.getItem(position).toString())
            var sendDays = gridView.adapter.getItem(position)
            val format = SimpleDateFormat("yyyy-MM-dd")
            val displayFormat = SimpleDateFormat("yyyy年MM月dd日(EEE)の予定")
            var calendarSendDate = format.format(sendDays)
            var calendarDisplayDate = displayFormat.format(sendDays)
            Log.d("position", sendDays.javaClass.kotlin.toString())
            val params = bundleOf("calendarSendDate" to calendarSendDate,"calendarDisplayDate" to calendarDisplayDate)
            findNavController()?.navigate(
                R.id.navigation_schedule,params
            )
        }
    }
    fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("position", position.toString())
    }


}