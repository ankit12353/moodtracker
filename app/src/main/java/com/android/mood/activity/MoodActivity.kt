package com.android.mood.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.android.mood.R
import com.android.mood.adapter.MoodAdapter
import com.android.mood.constants.Constant
import com.android.mood.constants.Constant.DATE
import com.android.mood.fragment.CustomizeMoodFragment
import com.android.mood.fragment.MoodTwoAddNewEntryFragment
import com.android.mood.model.MoodModel
import com.android.mood.utils.Utils
import kotlinx.android.synthetic.main.activity_mood.*
import kotlinx.android.synthetic.main.layout_topbar.*
import java.text.SimpleDateFormat
import java.util.*

class MoodActivity : AppCompatActivity() , MoodAdapter.MoodSelected{
    private var time : String?= null
    private var date : String ?= null
    private val moodList = arrayListOf<MoodModel>(
        MoodModel("Happy",R.drawable.happy),
        MoodModel("Meh",R.drawable.meh),
        MoodModel("Okay",R.drawable.okay),
        MoodModel("Sad",R.drawable.sad),
        MoodModel("Pain",R.drawable.pain)
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood)
        date = intent.extras?.getString(DATE)
        time = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
        tv_date_dialog.text = date!!
        tv_time_dialog.text = time!!
        tv_date_topbar.text = date!!

        container.visibility = View.VISIBLE
        container_fragment.visibility = View.GONE

        rv_mood.layoutManager= GridLayoutManager(this,5)
        rv_mood.setHasFixedSize(true)
        rv_mood.adapter= MoodAdapter(this,moodList,this)

        tv_time_dialog.setOnClickListener{openTimeDialog()}
        tv_date_dialog.setOnClickListener { openDateDialog() }
        back_btn_topbar.setOnClickListener{performBackBtn()}
        iv_customize_mood.setOnClickListener{customizeMood()}
    }

    override fun onMoodSelected(position: Int) {
        openMoodTwoFragment(position)
    }
    private fun customizeMood(){
        container.visibility = View.GONE
        container_fragment.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fragment, CustomizeMoodFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()

    }
     fun openMoodTwoFragment(moodPosition: Int) {
        container.visibility = View.GONE
        container_fragment.visibility = View.VISIBLE
        val moodTwoAddNewEntryFragment = MoodTwoAddNewEntryFragment()
        val bundle = Bundle()
        bundle.putString(Constant.DATE_MOOD_MOODTWO,date!!)
        bundle.putString(Constant.TIME_MOOD_MOODTWO,time!!)
        bundle.putString(Constant.MOODPOSITION_MOOD_MOODTWO,moodPosition.toString())
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

    fun getMoodDataOfPosition(position : Int) : MoodModel{
        val moodText = moodList[position].moodName
        val moodImage = moodList[position].moodImage
        return MoodModel(moodText,moodImage)
    }
    fun finishActivity() {
        finish()
    }
    private fun performBackBtn() {
        if(supportFragmentManager.backStackEntryCount>0){
//            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.currentFocus!!.windowToken,0)
            supportFragmentManager.popBackStack()
            container.visibility = View.VISIBLE
            container_fragment.visibility = View.GONE
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        performBackBtn()
    }
}