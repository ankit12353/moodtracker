package com.android.mood.model

data class MoodModel(val moodName: String , val moodImage: Int){
    private var isSelected : Boolean = false

    fun setSelected(selected : Boolean){
        isSelected = selected
    }
    fun isSelected() : Boolean{
        return isSelected
    }
}
