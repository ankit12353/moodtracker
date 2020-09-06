package com.android.mood.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.model.MoodModel

class MoodAdapter(private var mContext: Context,private var moods: ArrayList<MoodModel>,private val moodSelected: MoodSelected) : RecyclerView.Adapter<MoodAdapter.MoodHolder>() {

    private var row_index=-1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodHolder {
        val v =LayoutInflater.from(parent.context).inflate(R.layout.item_layout_mood,parent,false)
        return MoodHolder(v)
    }

    override fun getItemCount()= moods.size

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MoodHolder, position: Int) {
        holder.bind(moods[position])
        holder.llItemMood.setOnClickListener(View.OnClickListener {
            moodSelected.onMoodSelected(holder.adapterPosition)
            row_index=position
            notifyDataSetChanged()
        })
        if(row_index==position){
            holder.llInnerItemMood.setBackgroundColor(mContext.getColor(R.color.theme_color))
        }else {
            holder.llInnerItemMood.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    class MoodHolder(view : View) : RecyclerView.ViewHolder(view){
        private val moodImage = view.findViewById<ImageView>(R.id.iv_layout_mood)
        private val moodText = view.findViewById<TextView>(R.id.tv_layout_mood)
        val llItemMood = view.findViewById<LinearLayout>(R.id.ll_item_mood)
        val llInnerItemMood = view.findViewById<LinearLayout>(R.id.ll_inner_item_mood)
        fun bind(mood: MoodModel){
            moodText.text= mood.moodName
            moodImage.setImageResource(mood.moodImage)
        }
    }
    interface MoodSelected{
        fun onMoodSelected(position: Int)
    }
}