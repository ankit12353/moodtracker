package com.android.mood.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.helper.DataBaseHelper
import com.android.mood.adapter.AllEntryDetailAdapter
import com.android.mood.model.AllEntryDetailModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class EntriesFragmentBottomNavigation : Fragment(){
    private var tvNoEntryFound :TextView? = null
    private var v : View?=null
    private var mContext : Context? = null
    private var allEntriesList : ArrayList<AllEntryDetailModel> = ArrayList()
    private var adapter : AllEntryDetailAdapter?= null
    private var rvAllEntries : RecyclerView?= null
    private var dbHandler : DataBaseHelper?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_entries_navigationbar, container, false)

        init()
        noEntryTvVisibility()
        allEntriesList = dbHandler!!.getAllEntries()
        if(allEntriesList.size!= 0) tvNoEntryFound!!.visibility = View.GONE

        rvAllEntries!!.layoutManager = LinearLayoutManager(mContext!!)
        rvAllEntries!!.setHasFixedSize(true)
        adapter = AllEntryDetailAdapter(mContext!!, allEntriesList, tvNoEntryFound!!)
        rvAllEntries!!.adapter = adapter

        return v
    }

    private fun init(){
        rvAllEntries = v!!.findViewById(R.id.rv_allentries)
        tvNoEntryFound = v!!.findViewById(R.id.tv_no_entry_found_entryfrag)
        dbHandler = DataBaseHelper(mContext!!,null)
    }

    override fun onResume() {
        super.onResume()
        allEntriesList.removeAll(allEntriesList)
        allEntriesList.addAll(dbHandler!!.getAllEntries())
        noEntryTvVisibility()
        adapter!!.notifyDataSetChanged()

    }
    private fun noEntryTvVisibility(){
        if(allEntriesList.size == 0) {
            tvNoEntryFound!!.visibility = View.VISIBLE
            rvAllEntries!!.visibility = View.GONE
        } else {
            tvNoEntryFound!!.visibility = View.GONE
            rvAllEntries!!.visibility = View.VISIBLE
        }

    }
}