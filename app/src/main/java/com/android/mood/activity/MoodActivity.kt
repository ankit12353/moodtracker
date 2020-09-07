package com.android.mood.activity

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.android.mood.R
import com.android.mood.adapter.AllMoodDetailAdapter
import com.android.mood.adapter.MoodAdapter
import com.android.mood.constants.Constant
import com.android.mood.constants.Constant.DATE
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
    private val moods = arrayListOf<MoodModel>(
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
        time = SimpleDateFormat("KK:mm a", Locale.getDefault()).format(Date())
        tv_date_mood.text = date!!
        tv_time_mood.text = time!!
        tv_date_topbar.text = date!!

        container.visibility = View.VISIBLE
        container_fragment.visibility = View.GONE

        rv_mood.layoutManager= GridLayoutManager(this,5)
        rv_mood.setHasFixedSize(true)
        rv_mood.adapter= MoodAdapter(this,moods,this)

        tv_time_mood.setOnClickListener(View.OnClickListener {
            openTimeDialog()
        })
        back_btn_topbar.setOnClickListener(View.OnClickListener {
            performBackBtn()
        })

    }

    override fun onMoodSelected(position: Int) {
        openMoodTwoFragment(position)
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
        val mTimePicker = TimePickerDialog(this,object : TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                var am_pm = ""
                val datetime = Calendar.getInstance()
                datetime[Calendar.HOUR_OF_DAY] = hourOfDay
                datetime[Calendar.MINUTE] = minute
                if (datetime[Calendar.AM_PM] == Calendar.AM) am_pm ="AM"
                else if (datetime[Calendar.AM_PM] == Calendar.PM) am_pm = "PM"

                val hr = Utils().timeTotwelvehr(hourOfDay)
                time= Utils().formateTime(hr,minute,am_pm)
                tv_time_mood!!.text = time
            }
        },currentHour,currentMinute,false)
        mTimePicker.show()
    }

    fun getMoodDataOfPosition(position : Int) : MoodModel{
        val moodText = moods[position].moodName
        val moodImage = moods[position].moodImage
        return MoodModel(moodText,moodImage)
    }
    fun finishActivity() {
        finish()
    }
    private fun performBackBtn() {
        if(supportFragmentManager.backStackEntryCount>0){
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