package com.android.mood.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class MoodBitmapModel(val moodName: String , val moodImage: Bitmap) : Parcelable{
    private var isSelected : Boolean = false

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readParcelable(Bitmap::class.java.classLoader)!!
    ) {
        isSelected = parcel.readByte() != 0.toByte()
    }

    fun isSelected() : Boolean{
        return isSelected
    }
    fun setSelected(selected : Boolean){
        isSelected = selected
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(moodName)
        parcel.writeParcelable(moodImage, flags)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MoodBitmapModel> {
        override fun createFromParcel(parcel: Parcel): MoodBitmapModel {
            return MoodBitmapModel(parcel)
        }

        override fun newArray(size: Int): Array<MoodBitmapModel?> {
            return arrayOfNulls(size)
        }
    }
}