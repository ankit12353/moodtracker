package com.android.mood.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.activity.MoodActivity
import com.android.mood.adapter.AllMoodListAdapter
import com.android.mood.helper.DataBaseHelper
import com.android.mood.model.MoodBitmapModel


class AllMoodListFragment : Fragment(),AllMoodListAdapter.ModifyMoods{
    private var v : View?= null
    private var mContext : Context?= null
    private var rvAllMoodList : RecyclerView ?= null
    private var adapter : AllMoodListAdapter?= null
    private var allMoodList : ArrayList<MoodBitmapModel> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        v=  inflater.inflate(R.layout.fragment_all_mood_list, container, false)
        init()
        allMoodList = DataBaseHelper(mContext!!,null).getMoodList()
        rvAllMoodList!!.setHasFixedSize(true)
        rvAllMoodList!!.layoutManager = LinearLayoutManager(mContext!!)
        adapter = AllMoodListAdapter(allMoodList,mContext!!,this)
        rvAllMoodList!!.adapter = adapter!!

        v!!.findViewById<Button>(R.id.btn_addnewMood).setOnClickListener { (activity as MoodActivity).openAddNewMoodFragment() }

        return v
    }
    private fun init(){
        rvAllMoodList = v!!.findViewById(R.id.rv_allmoodlist)
    }

    override fun onUpdateClicked(updateMood: MoodBitmapModel) {
        (activity as MoodActivity).openUpdateMoodFragment(updateMood)
    }


}