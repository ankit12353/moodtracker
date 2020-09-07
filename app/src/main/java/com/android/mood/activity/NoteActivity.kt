package com.android.mood.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mood.constants.Constant.*
import com.android.mood.R
import com.android.mood.SqliteDBOpenHelper
import com.android.mood.adapter.AllMoodDetailAdapter
import com.android.mood.model.MoodDetailAllModel
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.layout_topbar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NoteActivity : AppCompatActivity() ,AllMoodDetailAdapter.PerformOperation{

    private var date: String? = null
    private var moodList : ArrayList<MoodDetailAllModel> = ArrayList()
    private var adapter : AllMoodDetailAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        date = intent.extras?.getString(DATE)
        tv_date_topbar.text = date
        getMoodOfSelectedDate()

        Collections.sort(moodList,Comparator<MoodDetailAllModel>{t1,t2 -> SimpleDateFormat("hh:mm a").parse(t1.time).compareTo(SimpleDateFormat("hh:mm a").parse(t2.time))})
        rv_entry_date.layoutManager = LinearLayoutManager(this)
        rv_entry_date.setHasFixedSize(true)
        adapter = AllMoodDetailAdapter(this,moodList,this)
        rv_entry_date.adapter = adapter

        back_btn_topbar.setOnClickListener(View.OnClickListener {
            finish()
        })

        rl_add_new_entry.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@NoteActivity,MoodActivity::class.java)
            intent.putExtra(DATE,date)
            startActivity(intent)
        })
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
