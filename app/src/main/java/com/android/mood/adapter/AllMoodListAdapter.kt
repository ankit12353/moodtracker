package com.android.mood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.fragment.AllMoodListFragment
import com.android.mood.helper.DataBaseHelper
import com.android.mood.model.MoodBitmapModel

class AllMoodListAdapter(private val allMoodList : ArrayList<MoodBitmapModel>,private val mContext : Context) : RecyclerView.Adapter<AllMoodListAdapter.ViewHolder>(){

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val ivMood = view.findViewById<ImageView>(R.id.iv_allmoodlist)
        private val tvMood = view.findViewById<TextView>(R.id.tv_allmoodlist)
        val ivModify = view.findViewById<ImageView>(R.id.iv_modify_allmoodlist)
        fun bind(mood : MoodBitmapModel){
            ivMood.setImageBitmap(mood.moodImage)
            tvMood.text = mood.moodName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_allmoodlist,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount()= allMoodList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allMoodList[position])
        holder.ivModify.setOnClickListener{ onModifySelected(holder.ivModify,allMoodList[holder.adapterPosition]) }
    }

    private fun onModifySelected(ivModify : ImageView,selectedMood : MoodBitmapModel){
        val popupMenu = PopupMenu(mContext, ivModify)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.popup_delete -> {
                    DataBaseHelper(mContext, null).deleteMood(selectedMood.moodName)
                    allMoodList.remove(selectedMood)
                    notifyDataSetChanged()
                }
                R.id.popup_update -> {

                }
            }
            true
        })
        popupMenu.show()
    }
}