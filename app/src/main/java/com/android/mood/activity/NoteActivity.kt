package com.android.mood.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.mood.constants.Constant.*
import com.android.mood.R
import com.android.mood.helper.DataBaseHelper
import com.android.mood.adapter.AllEntryDetailAdapter
import com.android.mood.model.AllEntryDetailModel
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.layout_topbar.*
import kotlin.collections.ArrayList

class NoteActivity : AppCompatActivity(){

    private var date: String? = null
    private var allEntryList : ArrayList<AllEntryDetailModel> = ArrayList()
    private var adapter : AllEntryDetailAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        date = intent.extras?.getString(DATE)
        tv_date_topbar.text = date
        allEntryList = DataBaseHelper(this,null).getEntryDetail(date!!)
        if(allEntryList.size !=0 ) tv_no_entry.visibility = View.GONE

        rv_entry_date.layoutManager = LinearLayoutManager(this)
        rv_entry_date.setHasFixedSize(true)
        adapter = AllEntryDetailAdapter(this,allEntryList,tv_no_entry)
        rv_entry_date.adapter = adapter

        back_btn_topbar.setOnClickListener{ finish() }

        rl_add_new_entry.setOnClickListener {
            val intent = Intent(this@NoteActivity, MoodActivity::class.java)
            intent.putExtra(DATE, date)
            startActivity(intent)
        }
    }
}
