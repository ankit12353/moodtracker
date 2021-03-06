package com.android.mood.model

import android.os.Parcel
import android.os.Parcelable

data class MoodModel(val moodName: String , val moodImage: Int) : Parcelable{
    private var isSelected : Boolean = false

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()
    ) {
        isSelected = parcel.readByte() != 0.toByte()
    }

    fun setSelected(selected : Boolean){
        isSelected = selected
    }
    fun isSelected() : Boolean{
        return isSelected
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(moodName)
        parcel.writeInt(moodImage)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MoodModel> {
        override fun createFromParcel(parcel: Parcel): MoodModel {
            return MoodModel(parcel)
        }

        override fun newArray(size: Int): Array<MoodModel?> {
            return arrayOfNulls(size)
        }
    }
}
