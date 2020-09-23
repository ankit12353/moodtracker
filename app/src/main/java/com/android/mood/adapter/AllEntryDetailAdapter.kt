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
import com.android.mood.helper.DataBaseHelper
import com.android.mood.model.MoodDetailAllModel

class AllEntryDetailAdapter(private val mContext: Context, private val allEntriesList : ArrayList<MoodDetailAllModel>, private val performOperation: PerformOperation) : RecyclerView.Adapter<AllEntryDetailAdapter.ViewHolder>(){
    val sqliteHelper = DataBaseHelper(mContext, null)
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val date = view.findViewById<TextView>(R.id.tv_date)
        val mood = view.findViewById<TextView>(R.id.tv_mood)
        val moodTwo = view.findViewById<TextView>(R.id.tv_mood_two)
        val moodImage = view.findViewById<ImageView>(R.id.iv_mood)
        val time = view.findViewById<TextView>(R.id.tv_time)
        val note = view.findViewById<TextView>(R.id.tv_note)
        val ivPerformOperation = view.findViewById<ImageView>(R.id.iv_update_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_entry,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return allEntriesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mood = allEntriesList[position]
        val list = sqliteHelper.getMoodList()
        holder.date.text = mood.date
        holder.mood.text = list[mood.moodPosition.toInt()].moodName
        holder.moodTwo.text = mood.moodTwo
        holder.moodImage.setImageBitmap(list[mood.moodPosition.toInt()].moodImage)
        holder.time.text = "("+mood.time+")"
        holder.note.text = "Note: "+mood.note
        holder.ivPerformOperation.setOnClickListener(View.OnClickListener {
            performOperation(allEntriesList[holder.adapterPosition],holder.ivPerformOperation)
        })
    }

    interface PerformOperation{
        fun delete(selectedMood : MoodDetailAllModel)
    }

    private fun performOperation(selectedMood : MoodDetailAllModel, ivPerformOperation: ImageView) {
        val popupMenu = PopupMenu(mContext, ivPerformOperation)
        val menu = popupMenu.menu
        popupMenu.menuInflater.inflate(R.menu.popup_menu, menu)
        menu.findItem(R.id.popup_update).isVisible = false

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.popup_delete ->{
                    sqliteHelper.deleteEntry(selectedMood.date,selectedMood.time)
                    performOperation.delete(selectedMood)
                }
            }
            true
        })
        popupMenu.show()
    }
}