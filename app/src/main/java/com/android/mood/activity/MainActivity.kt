package com.android.mood.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.android.mood.R
import com.android.mood.fragment.CalendarFragmentBottomNavigation
import com.android.mood.fragment.EntriesFragmentBottomNavigation
import com.android.mood.fragment.MoreFragmentBottomNavigation
import com.android.mood.fragment.NewsFragmentBottomNavigation
import com.android.mood.helper.Constant.DATE
import com.android.mood.helper.Constant.PREFERENCE
import com.android.mood.helper.DataBaseHelper
import com.android.mood.model.MoodModel
import com.android.mood.helper.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var calendarFragment: CalendarFragmentBottomNavigation
    private lateinit var newsFragment: NewsFragmentBottomNavigation
    private lateinit var entriesFragment: EntriesFragmentBottomNavigation
    private lateinit var moreFragment: MoreFragmentBottomNavigation
    private var sp : SharedPreferences?= null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sp = getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE)
        //async task to be added
        val isListSaved = sp!!.getBoolean(LIST_SAVED,false)
        if(isListSaved) {
            //do nothing
        } else { saveArrayLists()}


        entriesFragment = EntriesFragmentBottomNavigation()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_bottomnav, entriesFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_entries -> {
                    entriesFragment = EntriesFragmentBottomNavigation()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_bottomnav, entriesFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.navigation_calendar -> {
                    calendarFragment = CalendarFragmentBottomNavigation()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_bottomnav, calendarFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.navigation_add_entry -> {
                    val date = SimpleDateFormat("dd/MM/yyy").format(Date())
                    val intent = Intent(this@MainActivity, MoodActivity::class.java)
                    intent.putExtra(DATE, date)
                    startActivity(intent)
                }
                R.id.navigation_news -> {
                    newsFragment = NewsFragmentBottomNavigation()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_bottomnav, newsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.navigation_more -> {
                    moreFragment = MoreFragmentBottomNavigation()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_bottomnav, moreFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.container_bottomnav, entriesFragment)
//            .commit()
    }
    private fun saveArrayLists(){
        val moodList = arrayListOf<MoodModel>(
            MoodModel("Happy",R.drawable.happy),
            MoodModel("Meh",R.drawable.meh),
            MoodModel("Okay",R.drawable.okay),
            MoodModel("Sad",R.drawable.sad),
            MoodModel("Pain",R.drawable.pain)
        )
        val moodTwoList = arrayListOf<MoodModel>(
            MoodModel("Cycling", R.drawable.cycle),
            MoodModel("Reading", R.drawable.book),
            MoodModel("Cleaning", R.drawable.clean),
            MoodModel("Family", R.drawable.family),
            MoodModel("Friend", R.drawable.friend),
            MoodModel("Fruits", R.drawable.fruit),
            MoodModel("Gym", R.drawable.gym),
            MoodModel("Gaming", R.drawable.game),
            MoodModel("Movies", R.drawable.monitor),
            MoodModel("Jogging", R.drawable.run),
            MoodModel("Shopping", R.drawable.shopping),
            MoodModel("Yoga", R.drawable.yoga)
        )
        val dbHelper = DataBaseHelper(this,null)
        for (i in 0 until moodList.size){
            val name = moodList[i].moodName
            val image = Utils.drawableToByteArray(moodList[i].moodImage,this@MainActivity)
            dbHelper.addMood(name,image)
        }
        for (i in 0 until moodTwoList.size){
            val name = moodTwoList[i].moodName
            val image = Utils.drawableToByteArray(moodTwoList[i].moodImage,this)
            dbHelper.addMoodTwo(name, image)
        }
        sp!!.edit().putBoolean(LIST_SAVED,true).apply()
    }

    companion object{
        private const val LIST_SAVED = "lists"
    }
}
