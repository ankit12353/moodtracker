package com.android.mood.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R


class CustomizeMoodFragment : Fragment() {
    private var v : View?= null
    private var mContext : Context?= null
    private var moodName : String ?= null
    private var rvCustomizeMood : RecyclerView ?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        v=  inflater.inflate(R.layout.fragment_customize_mood, container, false)
        init()

        return v
    }
    private fun init(){
        rvCustomizeMood = v!!.findViewById(R.id.rv_customize_mood)
        val etMoodName = v!!.findViewById<EditText>(R.id.et_mood_name_customize)
        moodName = etMoodName.text.trim().toString()
    }
}