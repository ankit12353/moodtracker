package com.android.mood.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.activity.MoodActivity
import com.android.mood.adapter.CustomizeMoodAdapter
import com.android.mood.constants.Constant.MOODOBJECT_MOOD_MOODTWO
import com.android.mood.helper.DataBaseHelper
import com.android.mood.model.MoodBitmapModel
import com.android.mood.utils.Utils
import java.io.ByteArrayOutputStream


class AddUpdateMoodFragment : Fragment() ,CustomizeMoodAdapter.ImageSelected{
    private var v : View?= null
    private var mContext : Context?= null
    private var rvAddNewMood : RecyclerView?= null
    private var etAddNewMood : TextView?= null
    private var selectedImagePosi : Int? = null
    private var ivAddNewMood : ImageView ?= null
    private var isUpdate : Boolean = false
    private var updateMood : MoodBitmapModel?= null
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
        selectedImagePosi = position
        ivAddNewMood!!.setImageResource(customizeMoods[position])
    }
    private fun init(){
        rvAddNewMood = v!!.findViewById(R.id.rv_add_new_mood)
        ivAddNewMood = v!!.findViewById(R.id.iv_add_new_mood)
        etAddNewMood = v!!.findViewById(R.id.et_add_new_mood)
        if(arguments!= null && arguments?.containsKey(MOODOBJECT_MOOD_MOODTWO)!!){
            isUpdate = true
            updateMood = arguments?.getParcelable(MOODOBJECT_MOOD_MOODTWO)
            etAddNewMood!!.hint = updateMood!!.moodName
            ivAddNewMood!!.setImageBitmap(updateMood!!.moodImage)
        }
    }
    fun submitData(){
        val dbHelper = DataBaseHelper(mContext!!,null)
        val moodName = etAddNewMood!!.text.trim().toString()
        var ifNameExist : Boolean= false
        val allMoodList = dbHelper.getMoodList()
        for (i in 0 until allMoodList.size){
            if(allMoodList[i].moodName == moodName) {
                ifNameExist= true
                break
            }
        }
        when {
            moodName.isEmpty() -> { etAddNewMood!!.error = "Required"}
            ifNameExist -> { etAddNewMood!!.error ="Mood Name already exists" }
            !isUpdate && selectedImagePosi ==null -> { Toast.makeText(mContext!!,"Select any image", Toast.LENGTH_SHORT).show() }
            else -> {
                var moodImage : ByteArray ?= null
                if (selectedImagePosi!=null) moodImage= Utils().drawableToByteArray(customizeMoods[selectedImagePosi!!],mContext!!)
                if(isUpdate){
                    if(selectedImagePosi==null){
                        dbHelper.updateMoodName(updateMood!!.getId(),moodName)
                    } else {
                        dbHelper.updateMood(updateMood!!.getId(),moodName,moodImage!!)
                    }
                    (activity as MoodActivity).performBackBtn()
                } else {
                    dbHelper.addMood(moodName,moodImage!!)
                    (activity as MoodActivity).performBackBtn()
                }
            }
        }
    }

    fun clearData(){
        etAddNewMood!!.text = ""
        ivAddNewMood!!.setImageResource(R.drawable.circular_border)
        selectedImagePosi = null
        isUpdate = false
    }
}