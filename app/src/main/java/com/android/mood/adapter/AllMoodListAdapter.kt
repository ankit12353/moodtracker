package com.android.mood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.activity.MoodActivity
import com.android.mood.fragment.AllMoodListFragment
import com.android.mood.helper.DataBaseHelper
import com.android.mood.model.MoodBitmapModel

class AllMoodListAdapter(private val allMoodList : ArrayList<MoodBitmapModel>,private val mContext : Context,private val modifyMoods : ModifyMoods) : RecyclerView.Adapter<AllMoodListAdapter.ViewHolder>(){
    private val dbHelper = DataBaseHelper(mContext, null)
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val ivMood = view.findViewById<ImageView>(R.id.iv_allmoodlist)
        private val tvMood = view.findViewById<TextView>(R.id.tv_allmoodlist)
        val ivModify: ImageView = view.findViewById(R.id.iv_modify_allmoodlist)
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
        holder.ivModify.setOnClickListener{ onModifySelected(holder.ivModify,holder.adapterPosition) }
    }

    private fun onModifySelected(ivModify : ImageView,position: Int){
        val selectedMood = allMoodList[position]
        val popupMenu = PopupMenu(mContext, ivModify)
        val menu = popupMenu.menu
        popupMenu.menuInflater.inflate(R.menu.popup_menu, menu)
        deleteButtonFunctionality(menu,position)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.popup_delete -> {
                    dbHelper.deleteMood(selectedMood.moodName)
                    allMoodList.remove(selectedMood)
                    notifyDataSetChanged()
                }
                R.id.popup_update -> {
                    modifyMoods.onUpdateClicked(selectedMood)
                }
            }
            true
        })
        popupMenu.show()
    }

    private fun deleteButtonFunctionality(menu: Menu,position: Int){
        if(position<=4) {
            menu.findItem(R.id.popup_delete).isVisible = false
        }
        //if mood is being used delete feature
    }

    interface ModifyMoods{
        fun onUpdateClicked(updateMood : MoodBitmapModel)
    }
}