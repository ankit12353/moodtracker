package com.android.mood.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


class Utils {
    fun formateDate(date : Int,month : Int,year : Int) : String{
        val dateS : String
        val monthS : String
        val yearS : String = year.toString()
        if(date<10){
            dateS = "0"+(date.toString())
        } else {
            dateS = date.toString()
        }
        if(month<10){
            monthS = "0"+(month.toString())
        } else{
            monthS = month.toString()
        }

        val selectedDate = dateS+"/"+monthS+"/"+yearS
        return selectedDate
    }
    fun formateTime(h : Int,m : Int,ap : String) : String {
        val hour : String
        val minute : String
        if(h<10){
            hour="0"+(h.toString())
        } else {
            hour = h.toString()
        }
        if(m<10){
            minute = "0"+(m.toString())
        } else{
            minute = m.toString()
        }
        val time = hour+":"+minute+" "+ap
        return time
    }

    fun timeTotwelvehr(hourOfDay : Int) : Int{
        val hr : Int
        if(hourOfDay>12) hr= hourOfDay-12
        else if(hourOfDay==0) hr = 12
        else hr = hourOfDay
        return hr
    }

    fun drawableToByteArray(image : Int,mContext : Context) : ByteArray{
        val bitmap = BitmapFactory.decodeResource(mContext.resources,image)
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val img: ByteArray = bos.toByteArray()
        return img
    }
}