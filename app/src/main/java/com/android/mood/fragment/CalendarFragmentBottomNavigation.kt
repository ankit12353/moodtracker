package com.android.mood.fragment

import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import com.android.mood.constants.Constant.*
import com.android.mood.R
import com.android.mood.activity.NoteActivity
import com.android.mood.utils.Utils
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragmentBottomNavigation : Fragment() {
    private var mContext:Context?=null
    private var v:View?=null
    private var calendarView : CalendarView?= null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v =inflater.inflate(R.layout.fragment_calendar_navigationbar, container, false)

        init()
        val calendar = Calendar.getInstance()
        calendarView!!.maxDate = calendar.timeInMillis
        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val date = Utils().formateDate(dayOfMonth,(month+1),year)
            val intent = Intent(activity,NoteActivity::class.java)
            intent.putExtra(DATE,date)
            activity?.startActivity(intent)
        }
        return v
    }

    private fun init(){
        calendarView = v!!.findViewById<CalendarView>(R.id.calendar_view)
    }
}