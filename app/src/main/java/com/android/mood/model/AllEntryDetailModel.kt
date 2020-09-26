package com.android.mood.model

import android.os.Parcel
import android.os.Parcelable

data class AllEntryDetailModel(val date : String, val moodPosition : String, val moodTwo : String, val time :String, val note : String) : Parcelable{
    private var id : Int ?= null

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    fun setId(gId : Int){
        id = gId
    }
    fun getId() : Int{
        return id!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(moodPosition)
        parcel.writeString(moodTwo)
        parcel.writeString(time)
        parcel.writeString(note)
        parcel.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AllEntryDetailModel> {
        override fun createFromParcel(parcel: Parcel): AllEntryDetailModel {
            return AllEntryDetailModel(parcel)
        }

        override fun newArray(size: Int): Array<AllEntryDetailModel?> {
            return arrayOfNulls(size)
        }
    }
}