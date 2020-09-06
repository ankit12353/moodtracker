package com.android.mood.model

import android.os.Parcel
import android.os.Parcelable

data class MoodDetailAllModel(val date : String,val moodPosition : String , val moodTwo : String, val time :String,val note : String) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(moodPosition)
        parcel.writeString(moodTwo)
        parcel.writeString(time)
        parcel.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MoodDetailAllModel> {
        override fun createFromParcel(parcel: Parcel): MoodDetailAllModel {
            return MoodDetailAllModel(parcel)
        }

        override fun newArray(size: Int): Array<MoodDetailAllModel?> {
            return arrayOfNulls(size)
        }
    }
}