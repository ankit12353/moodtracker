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
import com.android.mood.model.MoodBitmapModel
import com.android.mood.model.AllEntryDetailModel

class AllEntryDetailAdapter(private val mContext: Context, private val allEntriesList : ArrayList<AllEntryDetailModel>, private val tvNoEntry :TextView) : RecyclerView.Adapter<AllEntryDetailAdapter.ViewHolder>(){
    val sqliteHelper = DataBaseHelper(mContext, null)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_entry,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return allEntriesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(allEntriesList[position],sqliteHelper.getMoodList())
        holder.ivPerformOperation.setOnClickListener(View.OnClickListener {
            performOperation(allEntriesList[holder.adapterPosition],holder.ivPerformOperation)
        })
    }
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val date = view.findViewById<TextView>(R.id.tv_date)
        private val moodName = view.findViewById<TextView>(R.id.tv_mood)
        private val moodTwoName = view.findViewById<TextView>(R.id.tv_mood_two)
        private val moodImage = view.findViewById<ImageView>(R.id.iv_mood)
        private val time = view.findViewById<TextView>(R.id.tv_time)
        private val note = view.findViewById<TextView>(R.id.tv_note)
        val ivPerformOperation: ImageView = view.findViewById(R.id.iv_update_delete)

        fun bind(entryObj : AllEntryDetailModel, moodList : ArrayList<MoodBitmapModel>){
            date.text = entryObj.date
            moodName.text = moodList[entryObj.moodPosition.toInt()].moodName
            moodTwoName.text = entryObj.moodTwo
            moodImage.setImageBitmap(moodList[entryObj.moodPosition.toInt()].moodImage)
            time.text = "("+entryObj.time+")"
            note.text = "Note: "+entryObj.note
        }
    }

    private fun performOperation(selectedMood : AllEntryDetailModel, ivPerformOperation: ImageView) {
        val popupMenu = PopupMenu(mContext, ivPerformOperation)
        val menu = popupMenu.menu
        popupMenu.menuInflater.inflate(R.menu.popup_menu, menu)
        menu.findItem(R.id.popup_update).isVisible = false

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.popup_delete ->{
                    sqliteHelper.deleteEntry(selectedMood.getId())
                    allEntriesList.remove(selectedMood)
                    notifyDataSetChanged()
                    if(allEntriesList.size==0) tvNoEntry.visibility = View.VISIBLE
                }
            }
            true
        })
        popupMenu.show()
    }
}