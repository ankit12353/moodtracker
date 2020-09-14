package com.android.mood.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.android.mood.R
import com.android.mood.constants.Constant.DATE
import com.android.mood.fragment.CalendarFragmentBottomNavigation
import com.android.mood.fragment.EntriesFragmentBottomNavigation
import com.android.mood.fragment.MoreFragmentBottomNavigation
import com.android.mood.fragment.StatsFragmentBottomNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
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
}
