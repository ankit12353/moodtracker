package com.android.mood.fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.SqliteDBOpenHelper
import com.android.mood.activity.NoteActivity
import com.android.mood.adapter.AllMoodDetailAdapter
import com.android.mood.model.MoodDetailAllModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class EntriesFragmentBottomNavigation : Fragment() ,AllMoodDetailAdapter.PerformOperation{
    private var tvNoEntryFound :TextView? = null
    private var v : View?=null
    private var mContext : Context? = null
    private var moodList : ArrayList<MoodDetailAllModel> = ArrayList()
    private var rvAllEntries : RecyclerView?= null
    private var adapter : AllMoodDetailAdapter?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_entries_navigationbar, container, false)

        init()
        getAllMoodDetailsFromDatabase()
        rvAllEntries!!.layoutManager = LinearLayoutManager(mContext!!)
        rvAllEntries!!.setHasFixedSize(true)
        adapter = AllMoodDetailAdapter(mContext!!, moodList, this)
        rvAllEntries!!.adapter = adapter

        return v
    }

    private fun init(){
        rvAllEntries = v!!.findViewById<RecyclerView>(R.id.rv_allentries)
        tvNoEntryFound = v!!.findViewById(R.id.tv_no_entry_found_entryfrag)
    }

    private fun getAllMoodDetailsFromDatabase(){
        val dbHandler = SqliteDBOpenHelper(mContext!!,null)
        val result = dbHandler.getMoodDetailsAll()
        if(result.moveToFirst()){
            do {
                val date = result.getString(result.getColumnIndex(SqliteDBOpenHelper.COLUMN_DATE))
                val moodPosition = result.getString(result.getColumnIndex(SqliteDBOpenHelper.COLUMN_MOOD_POSI))
                val moodTwo = result.getString(result.getColumnIndex(SqliteDBOpenHelper.COLUMN_MOOD_TWO))
                val time = result.getString(result.getColumnIndex(SqliteDBOpenHelper.COLUMN_TIME))
                val note = result.getString(result.getColumnIndex(SqliteDBOpenHelper.COLUMN_NOTE))
                moodList.add(MoodDetailAllModel(date,moodPosition,moodTwo,time,note))
            } while (result.moveToNext())
        }
        Collections.sort(moodList,Comparator<MoodDetailAllModel> { o1, o2 -> SimpleDateFormat("dd/MM/yyyyhh:mm a").parse(o1.date+o1.time).compareTo(SimpleDateFormat("dd/MM/yyyyhh:mm a").parse(o2.date+o2.time)) })
        if(moodList.size!= 0) tvNoEntryFound!!.visibility = View.GONE
    }

    override fun delete(selectedMood : MoodDetailAllModel) {
        moodList.remove(selectedMood)
        adapter!!.notifyDataSetChanged()
        if(moodList.size==0) tvNoEntryFound!!.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        moodList.removeAll(moodList)
        getAllMoodDetailsFromDatabase()
        adapter!!.notifyDataSetChanged()
    }

}