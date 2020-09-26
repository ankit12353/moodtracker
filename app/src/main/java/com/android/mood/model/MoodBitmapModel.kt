package com.android.mood.model

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class MoodBitmapModel(val moodName: String , val moodImage: Bitmap) : Parcelable{
    private var isSelected : Boolean = false
    private var id : Int ?= null

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readParcelable(Bitmap::class.java.classLoader)!!
    ) {
        isSelected = parcel.readByte() != 0.toByte()
        id = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    fun isSelected() : Boolean{
        return isSelected
    }
    fun setSelected(selected : Boolean){
        isSelected = selected
    }
    fun setId(gId : Int){
        id = gId
    }
    fun getId() : Int{
        return id!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(moodName)
        parcel.writeParcelable(moodImage, flags)
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeValue(id)
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