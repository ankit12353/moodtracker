package com.android.mood.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.activity.NoteActivity
import com.android.mood.adapter.MoodAdapter
import com.android.mood.constants.Constant
import com.android.mood.model.MoodModel
import com.android.mood.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class MoodAddNewEntryFragment : Fragment(),MoodAdapter.MoodSelected {
    private var mContext: Context?=null
    private var v:View?=null
    private var date: String ?= null
    private var time: String ?= null
    private var rvMood : RecyclerView ? = null
    private var tvDate : TextView ?= null
    private var tvTime : TextView ?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext=context
    }
    private val moods = arrayListOf<MoodModel>(
        MoodModel("Happy",R.drawable.happy),
        MoodModel("Meh",R.drawable.meh),
        MoodModel("Okay",R.drawable.okay),
        MoodModel("Sad",R.drawable.sad),
        MoodModel("Pain",R.drawable.pain)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.fragment_mood_add_new_entry, container, false)

        init()

        time = SimpleDateFormat("KK:mm a",Locale.getDefault()).format(Date())
        tvDate!!.text = date!!
        tvTime!!.text = time!!

        rvMood!!.layoutManager=GridLayoutManager(mContext,5)
        rvMood!!.setHasFixedSize(true)
        rvMood!!.adapter= MoodAdapter(mContext!!,moods,this)

        tvDate!!.setOnClickListener(View.OnClickListener {
            //openDateDialog()
        })
        tvTime!!.setOnClickListener(View.OnClickListener {
            openTimeDialog()
        })
        return v
    }

    fun getMoodDataOfPosition(position : Int) : MoodModel{
        val moodText = moods[position].moodName
        val moodImage = moods[position].moodImage
        return MoodModel(moodText,moodImage)
    }

    private fun init(){
        rvMood = v!!.findViewById<RecyclerView>(R.id.rv_mood)
        tvDate = v!!.findViewById(R.id.tv_date_moodfrag)
        tvTime = v!!.findViewById(R.id.tv_time_moodfrag)
        date = arguments?.getString(Constant.DATE_CALFRAG_NOTE)
    }

    override fun onMoodSelected(position: Int) {
        (activity as NoteActivity).openMoodTwoFragment(position,time!!)
    }

    private fun openDateDialog(){
        val dateSplit = date!!.split("/")
        val dd = DatePickerDialog(mContext!!,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            date = Utils().formateDate(dayOfMonth,month,year)
            tvDate!!.text = date
        },dateSplit[2].toInt(),dateSplit[1].toInt(),dateSplit[0].toInt())
        dd.show()
    }

    private fun openTimeDialog(){
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)
        val mTimePicker = TimePickerDialog(mContext,object : TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                var am_pm = ""
                val datetime = Calendar.getInstance()
                datetime[Calendar.HOUR_OF_DAY] = hourOfDay
                datetime[Calendar.MINUTE] = minute
                if (datetime[Calendar.AM_PM] == Calendar.AM) am_pm ="AM"
                else if (datetime[Calendar.AM_PM] == Calendar.PM) am_pm = "PM"

                val hr = Utils().timeTotwelvehr(hourOfDay)
                time= Utils().formateTime(hr,minute,am_pm)
                tvTime!!.text = time!!
            }
        },currentHour,currentMinute,false)
        mTimePicker.show()
    }
}