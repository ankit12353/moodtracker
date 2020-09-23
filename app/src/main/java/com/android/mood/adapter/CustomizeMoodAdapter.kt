package com.android.mood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R

class CustomizeMoodAdapter(private val moods : Array<Int>,private val mContext : Context,private val imageSelected : ImageSelected) : RecyclerView.Adapter<CustomizeMoodAdapter.ViewHolder>(){

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val ivMood = view.findViewById<ImageView>(R.id.iv_layout_mood)
        val tvMood = view.findViewById<TextView>(R.id.tv_layout_mood)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.item_layout_mood,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount()= moods.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvMood.visibility= View.GONE
        holder.ivMood.setImageResource(moods[position])
        holder.itemView.setOnClickListener{imageSelected.onImageSelected(holder.adapterPosition)}
    }

    interface ImageSelected{
        fun onImageSelected(position: Int)
    }
}