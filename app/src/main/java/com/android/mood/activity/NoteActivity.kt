package com.android.mood.activity

import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mood.constants.Constant.*
import com.android.mood.R
import com.android.mood.SqliteDBOpenHelper
import com.android.mood.adapter.AllMoodDetailAdapter
import com.android.mood.fragment.MoodAddNewEntryFragment
import com.android.mood.fragment.MoodTwoAddNewEntryFragment
import com.android.mood.model.MoodDetailAllModel
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.item_layout_entry.*
import kotlinx.android.synthetic.main.layout_topbar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NoteActivity : AppCompatActivity() ,AllMoodDetailAdapter.PerformOperation{

    private var date: String? = null
    private var moodAddNewEntryFragment: MoodAddNewEntryFragment = MoodAddNewEntryFragment()
    private var moodList : ArrayList<MoodDetailAllModel> = ArrayList()
    private var adapter : AllMoodDetailAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        rl_container_fragment.visibility = View.GONE
        date = intent.extras?.getString(DATE_CALFRAG_NOTE)
        tv_selectedDate.text = date
        getMoodOfSelectedDate()

        Collections.sort(moodList,Comparator<MoodDetailAllModel>{t1,t2 -> SimpleDateFormat("hh:mm a").parse(t1.time).compareTo(SimpleDateFormat("hh:mm a").parse(t2.time))})
        rv_entry_date.layoutManager = LinearLayoutManager(this)
        rv_entry_date.setHasFixedSize(true)
        adapter = AllMoodDetailAdapter(this,moodList,this)
        rv_entry_date.adapter = adapter

        back_btn.setOnClickListener(View.OnClickListener {
            performBackBtn()
        })

        rl_add_new_entry.setOnClickListener(View.OnClickListener {
            addNewEntry()
        })
    }

    fun addNewEntry() {
        rl_container_fragment.visibility = View.VISIBLE
        rl_entry_detail.visibility = View.GONE
        val bundle = Bundle()
        bundle.putString(DATE_CALFRAG_NOTE,date)
        moodAddNewEntryFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .add(R.id.rl_container_fragment, moodAddNewEntryFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    fun openMoodTwoFragment(moodPosition: Int,time : String) {
        val moodTwoAddNewEntryFragment = MoodTwoAddNewEntryFragment()
        val bundle = Bundle()
        bundle.putString(DATE_MOOD_MOODTWO,date)
        bundle.putString(TIME_MOOD_MOODTWO,time)
        bundle.putString(MOODPOSITION_MOOD_MOODTWO,moodPosition.toString())
        moodTwoAddNewEntryFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.rl_container_fragment, moodTwoAddNewEntryFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    private fun performBackBtn() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    fun finishActivity() {
        finish()
    }

    private fun getMoodOfSelectedDate() {
        val dbHandler = SqliteDBOpenHelper(this, null)
        val result = dbHandler.getMoodDetail(date!!)
        if (result.moveToFirst()) {
            do {
                rv_entry_date.visibility = View.VISIBLE
                tvNoEntryFound.visibility = View.GONE
                val moodPosition = result.getString(result.getColumnIndex(SqliteDBOpenHelper.COLUMN_MOOD_POSI))
                val moodTwo = result.getString(result.getColumnIndex(SqliteDBOpenHelper.COLUMN_MOOD_TWO))
                val time = result.getString(result.getColumnIndex(SqliteDBOpenHelper.COLUMN_TIME))
                val note = result.getString(result.getColumnIndex(SqliteDBOpenHelper.COLUMN_NOTE))
                moodList.add(MoodDetailAllModel(date!!,moodPosition,moodTwo,time,note))
            } while (result.moveToNext())
        } else {
            rv_entry_date.visibility = View.GONE
        }
    }

    override fun delete(selectedMood : MoodDetailAllModel) {
        moodList.remove(selectedMood)
        adapter!!.notifyDataSetChanged()
        if(moodList.size==0) tvNoEntryFound.visibility= View.VISIBLE
    }

}
