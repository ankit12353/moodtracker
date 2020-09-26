package com.android.mood.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.android.mood.R
import com.android.mood.adapter.MoodAdapter
import com.android.mood.constants.Constant.*
import com.android.mood.fragment.AddUpdateMoodFragment
import com.android.mood.fragment.AllMoodListFragment
import com.android.mood.fragment.MoodTwoAddNewEntryFragment
import com.android.mood.helper.DataBaseHelper
import com.android.mood.model.MoodBitmapModel
import com.android.mood.utils.Utils
import kotlinx.android.synthetic.main.activity_mood.*
import kotlinx.android.synthetic.main.layout_topbar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MoodActivity : AppCompatActivity() , MoodAdapter.MoodSelected{
    private var time : String?= null
    private var date : String ?= null
    private var adapter : MoodAdapter?= null
    private var moodList : ArrayList<MoodBitmapModel> = ArrayList()
    private val addNewMoodFragment = AddUpdateMoodFragment()
    private var dbHelper : DataBaseHelper?= null
    private val moodTwoAddNewEntryFragment = MoodTwoAddNewEntryFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood)

        dbHelper= DataBaseHelper(this,null)
        moodList = dbHelper!!.getMoodList()
        date = intent.extras?.getString(DATE)

        time = SimpleDateFormat("KK:mm a", Locale.getDefault()).format(Date())
        tv_date_dialog.text = date!!
        tv_time_dialog.text = time!!
        tv_date_topbar.text = date!!
        iv_forward_btn.visibility = View.GONE

        container.visibility = View.VISIBLE
        container_fragment.visibility = View.GONE

        rv_mood.layoutManager= GridLayoutManager(this,5)
        rv_mood.setHasFixedSize(true)
        adapter = MoodAdapter(this,moodList,this)
        rv_mood.adapter= adapter

        tv_time_dialog.setOnClickListener{openTimeDialog()}
        tv_date_dialog.setOnClickListener { openDateDialog() }
        back_btn_topbar.setOnClickListener{performBackBtn()}
        edit_mood.setOnClickListener{openAllMoodFragment()}
        iv_forward_btn.setOnClickListener{ goForward() }
    }

    override fun onMoodSelected(position: Int) {
        openMoodTwoFragment(position)
    }

    private fun openAllMoodFragment(){
        tv_date_topbar.visibility = View.GONE
        container.visibility = View.GONE
        container_fragment.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, AllMoodListFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    fun openAddNewMoodFragment(){
        iv_forward_btn.visibility = View.VISIBLE
        addNewMoodFragment.arguments = null
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, addNewMoodFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    fun openUpdateMoodFragment(updateMood : MoodBitmapModel){
        iv_forward_btn.visibility = View.VISIBLE
        val bundle = Bundle()
        bundle.putParcelable(MOODOBJECT_MOOD_MOODTWO,updateMood)
        addNewMoodFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, addNewMoodFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    private fun openMoodTwoFragment(moodPosition: Int) {
        iv_forward_btn.visibility = View.VISIBLE
        container.visibility = View.GONE
        container_fragment.visibility = View.VISIBLE
        val bundle = Bundle()
        bundle.putString(DATE_MOOD_MOODTWO,date!!)
        bundle.putString(TIME_MOOD_MOODTWO,time!!)
        bundle.putParcelable(MOODOBJECT_MOOD_MOODTWO,MoodBitmapModel(moodList[moodPosition].moodName,moodList[moodPosition].moodImage))
        bundle.putString(MOODPOSITION_MOOD_MOODTWO,moodPosition.toString())
        moodTwoAddNewEntryFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, moodTwoAddNewEntryFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    private fun openTimeDialog(){
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)
        val mTimePicker = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                var am_pm = ""
                val datetime = Calendar.getInstance()
                datetime[Calendar.HOUR_OF_DAY] = hourOfDay
                datetime[Calendar.MINUTE] = minute
                if (datetime[Calendar.AM_PM] == Calendar.AM) am_pm ="AM"
                else if (datetime[Calendar.AM_PM] == Calendar.PM) am_pm = "PM"

                val hr = Utils().timeTotwelvehr(hourOfDay)
                time= Utils().formateTime(hr,minute,am_pm)
                tv_time_dialog!!.text = time
            },currentHour,currentMinute,false)
        mTimePicker.show()
    }

    private fun openDateDialog() {
        val c = Calendar.getInstance()
        val mYear = c[Calendar.YEAR]
        val mDay = c[Calendar.DAY_OF_MONTH]
        val mMonth = c[Calendar.MONTH]

        val mDatePicker = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                date = Utils().formateDate(dayOfMonth,(month+1),year)
                tv_date_dialog.text = date!!
                tv_date_topbar.text = date!!
            },mYear,mMonth,mDay)
        mDatePicker.datePicker.maxDate= c.timeInMillis
        mDatePicker.show()
    }

    private fun goForward() {
        if (addNewMoodFragment.isVisible) {
            addNewMoodFragment.submitData()
        } else if(moodTwoAddNewEntryFragment.isVisible){
            moodTwoAddNewEntryFragment.getSelectedMoodTwo()
        }
    }

    fun finishActivity() {
        finish()
    }

    fun performBackBtn() {
        when {
            addNewMoodFragment.isVisible -> {
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.currentFocus!!.windowToken,0)
                addNewMoodFragment.clearData()
                supportFragmentManager.popBackStack()
                iv_forward_btn.visibility = View.GONE
            }
            moodTwoAddNewEntryFragment.isVisible -> {
                finish()
            }
            supportFragmentManager.backStackEntryCount>0 -> {
                supportFragmentManager.popBackStack()
                container.visibility = View.VISIBLE
                container_fragment.visibility = View.GONE
                iv_forward_btn.visibility = View.GONE
                tv_date_topbar.visibility = View.VISIBLE
                moodList.removeAll(moodList)
                moodList.addAll(dbHelper!!.getMoodList())
                adapter!!.notifyDataSetChanged()
            }
            else -> {
                finish()
            }
        }
    }

    override fun onBackPressed() {
        performBackBtn()
    }
}