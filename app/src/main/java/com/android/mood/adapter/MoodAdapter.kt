package com.android.mood.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.model.MoodBitmapModel
import com.android.mood.model.MoodModel

class MoodAdapter(private val mContext: Context, private val moodList : ArrayList<MoodBitmapModel>, private val moodSelected : MoodSelected) : RecyclerView.Adapter<MoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_mood,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount() = moodList.size

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mood = moodList[position]
            holder.ivMood.setImageBitmap(mood.moodImage)
        holder.tvMood.text = mood.moodName
        holder.rlItemMood.setOnClickListener(View.OnClickListener {
            moodSelected.onMoodSelected(holder.adapterPosition)
            mood.setSelected(!mood.isSelected())
            holder.rlItemMood.setBackgroundColor(if(mood.isSelected()) mContext.resources.getColor(R.color.theme_color) else Color.TRANSPARENT)
        })
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val ivMood = view.findViewById<ImageView>(R.id.iv_layout_mood)
        val tvMood = view.findViewById<TextView>(R.id.tv_layout_mood)
        val rlItemMood = view.findViewById<RelativeLayout>(R.id.rl_item_mood)
    }
    interface MoodSelected{
        fun onMoodSelected(position: Int)
    }
}