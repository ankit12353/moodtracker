package com.android.mood.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.android.mood.R
import com.android.mood.constants.Constant.*
import com.android.mood.fragment.CalendarFragmentBottomNavigation
import com.android.mood.fragment.EntriesFragmentBottomNavigation
import com.android.mood.fragment.MoreFragmentBottomNavigation
import com.android.mood.fragment.StatsFragmentBottomNavigation
import com.android.mood.helper.DataBaseHelper
import com.android.mood.model.MoodModel
import com.android.mood.utils.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var calendarFragment: CalendarFragmentBottomNavigation
    private lateinit var statsFragment: StatsFragmentBottomNavigation
    private lateinit var entriesFragment: EntriesFragmentBottomNavigation
    private lateinit var moreFragment: MoreFragmentBottomNavigation

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Paper.init(this)

        //async task to be added
        if(Paper.book().read<String>(LIST_SAVED) == "yes") {
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
                R.id.navigation_stats -> {
                    statsFragment = StatsFragmentBottomNavigation()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_bottomnav, statsFragment)
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
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_bottomnav, entriesFragment)
            .commit()
    }
    fun saveArrayLists(){
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
            val image = Utils().drawableToByteArray(moodList[i].moodImage,this@MainActivity)
            dbHelper.addMood(name,image)
        }
        for (i in 0 until moodTwoList.size){
            val name = moodTwoList[i].moodName
            val image = Utils().drawableToByteArray(moodTwoList[i].moodImage,this)
            dbHelper.addMoodTwo(name, image)
        }
        Paper.book().write(LIST_SAVED,"yes")
    }
}
