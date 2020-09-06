package com.android.mood.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.android.mood.R
import com.android.mood.constants.Constant
import com.android.mood.constants.Constant.DATE_CALFRAG_NOTE
import com.android.mood.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var calendarFragment : CalendarFragmentBottomNavigation
    private lateinit var statsFragment : StatsFragmentBottomNavigation
    private lateinit var entriesFragment: EntriesFragmentBottomNavigation
    private lateinit var moreFragment : MoreFragmentBottomNavigation

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        entriesFragment= EntriesFragmentBottomNavigation()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_bottomnav,entriesFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        val bottomNavigationView : BottomNavigationView=findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.navigation_entries -> {
                    entriesFragment= EntriesFragmentBottomNavigation()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_bottomnav,entriesFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.navigation_calendar -> {
                    calendarFragment= CalendarFragmentBottomNavigation()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_bottomnav,calendarFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.navigation_add_entry -> {
                    val date = SimpleDateFormat("dd/MM/yyy").format(Date())
                    //val time = SimpleDateFormat("HH:mm").format(Date())
                    val intent = Intent(this@MainActivity,NoteActivity::class.java)
                    intent.putExtra(DATE_CALFRAG_NOTE,date)
                    startActivity(intent)
                }
                R.id.navigation_stats -> {
                    statsFragment= StatsFragmentBottomNavigation()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_bottomnav,statsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.navigation_more -> {
                    moreFragment= MoreFragmentBottomNavigation()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_bottomnav,moreFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
            true
        }
    }
}
