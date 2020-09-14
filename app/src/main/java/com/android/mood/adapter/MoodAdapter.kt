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
import com.android.mood.model.MoodModel

class MoodAdapter(private val mContext: Context, private val moodList : ArrayList<MoodModel>, private val moodSelected : MoodSelected) : RecyclerView.Adapter<MoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_mood,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount() = moodList.size

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mood = moodList[position]
            holder.ivMoodTwo.setImageResource(mood.moodImage)
        holder.tvMoodTwo.text = mood.moodName
        holder.rlItemMoodTwo.setOnClickListener(View.OnClickListener {
            moodSelected.onMoodSelected(holder.adapterPosition)
            mood.setSelected(!mood.isSelected())
            holder.rlItemMoodTwo.setBackgroundColor(if(mood.isSelected()) mContext.resources.getColor(R.color.theme_color) else Color.TRANSPARENT)
        })
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val ivMoodTwo = view.findViewById<ImageView>(R.id.iv_layout_mood_two)
        val tvMoodTwo = view.findViewById<TextView>(R.id.tv_layout_mood_two)
        val rlItemMoodTwo = view.findViewById<RelativeLayout>(R.id.rl_item_mood_two)
    }
    interface MoodSelected{
        fun onMoodSelected(position: Int)
    }
}