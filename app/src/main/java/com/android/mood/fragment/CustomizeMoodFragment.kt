package com.android.mood.fragment

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.activity.MoodActivity
import com.android.mood.adapter.CustomizeMoodAdapter
import com.android.mood.helper.DataBaseHelper
import com.android.mood.model.MoodBitmapModel
import com.android.mood.model.MoodModel
import com.android.mood.utils.Utils


class CustomizeMoodFragment : Fragment(),CustomizeMoodAdapter.ImageSelected {
    private var v : View?= null
    private var mContext : Context?= null
    private var moodPosi : Int ?= null
    private var rvCustomizeMood : RecyclerView ?= null
    private var etMoodName : EditText ?= null
    private val customizeMoods = arrayOf(R.drawable.happy,R.drawable.meh)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        v=  inflater.inflate(R.layout.fragment_customize_mood, container, false)
        init()

        rvCustomizeMood!!.setHasFixedSize(true)
        rvCustomizeMood!!.layoutManager = GridLayoutManager(mContext!!,5)
        rvCustomizeMood!!.adapter=CustomizeMoodAdapter(customizeMoods,mContext!!,this)

        return v
    }
    private fun init(){
        rvCustomizeMood = v!!.findViewById(R.id.rv_customize_mood)
        etMoodName = v!!.findViewById(R.id.et_mood_name_customize)
    }

    override fun onImageSelected(position: Int) {
        moodPosi = position
        v!!.findViewById<ImageView>(R.id.iv_mood_image).setImageResource(customizeMoods[position])
    }

    fun submitData(){
        val dbHelper = DataBaseHelper(mContext!!,null)
        val moodName = etMoodName!!.text.trim().toString()
        if(moodName.isEmpty()) { etMoodName!!.error = "Required"}
        else if (moodPosi ==null) { Toast.makeText(mContext!!,"Select any image",Toast.LENGTH_SHORT).show() }
        else {
            val moodImage = Utils().drawableToByteArray(customizeMoods[moodPosi!!],mContext!!)
            dbHelper.addMood(moodName,moodImage)
            (activity as MoodActivity).getCustomizeMood(MoodBitmapModel(moodName,BitmapFactory.decodeByteArray(moodImage, 0, moodImage.size)))
        }
    }
}