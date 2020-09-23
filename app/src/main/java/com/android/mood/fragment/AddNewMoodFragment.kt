package com.android.mood.fragment

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.activity.MoodActivity
import com.android.mood.adapter.CustomizeMoodAdapter
import com.android.mood.helper.DataBaseHelper
import com.android.mood.model.MoodBitmapModel
import com.android.mood.utils.Utils

class AddNewMoodFragment : Fragment() ,CustomizeMoodAdapter.ImageSelected{
    private var v : View?= null
    private var mContext : Context?= null
    private var rvAddNewMood : RecyclerView?= null
    private var etAddNewMood : TextView?= null
    private var moodPosi : Int? = null
    private val customizeMoods = arrayOf(R.drawable.happy,R.drawable.meh)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        v =  inflater.inflate(R.layout.fragment_add_new_mood, container, false)
        init()
        rvAddNewMood!!.setHasFixedSize(true)
        rvAddNewMood!!.layoutManager = GridLayoutManager(mContext!!,5)
        rvAddNewMood!!.adapter= CustomizeMoodAdapter(customizeMoods,mContext!!,this)
        return v
    }

    override fun onImageSelected(position: Int) {
        moodPosi = position
        v!!.findViewById<ImageView>(R.id.iv_add_new_mood).setImageResource(customizeMoods[position])
    }
    private fun init(){
        rvAddNewMood = v!!.findViewById(R.id.rv_add_new_mood)
        etAddNewMood = v!!.findViewById(R.id.et_add_new_mood)
        etAddNewMood!!.requestFocus()
    }
    fun submitData(){
        val dbHelper = DataBaseHelper(mContext!!,null)
        val moodName = etAddNewMood!!.text.trim().toString()
        when {
            moodName.isEmpty() -> { etAddNewMood!!.error = "Required"}
            moodPosi ==null -> { Toast.makeText(mContext!!,"Select any image", Toast.LENGTH_SHORT).show() }
            else -> {
                val moodImage = Utils().drawableToByteArray(customizeMoods[moodPosi!!],mContext!!)
                val a = dbHelper.addMood(moodName,moodImage)
                if(a<0 && a>-2){
                    etAddNewMood!!.error = "Mood Name already exists"
                } else {
                    (activity as MoodActivity).getCustomizeMood(
                        MoodBitmapModel(moodName,BitmapFactory.decodeByteArray(moodImage, 0, moodImage.size))
                    )
                }
            }
        }
    }
}