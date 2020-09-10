package com.android.mood.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.android.mood.model.MoodDetailAllModel


class DataBaseHelper(mContext : Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(mContext ,
    DATABASE_NAME,factory,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableMood = "CREATE TABLE $TABLE_NAME ($COLUMN_DATE TEXT ,$COLUMN_TIME TEXT,$COLUMN_MOOD_POSI TEXT,$COLUMN_MOOD_TWO TEXT,$COLUMN_NOTE TEXT)"
        val tableMoodList = "CREATE TABLE $TABLE_MOOD_LIST($MOOD_NAME TEXT ,$MOOD_IMAGE BLOB)"
        val tableMoodTwoList = "CREATE TABLE $TABLE_MOODTWO_LIST($MOODTWO_NAME TEXT ,$MOODTWO_IMAGE BLOB)"
        db!!.execSQL(createTableMood)
        db.execSQL(tableMoodList)
        db.execSQL(tableMoodTwoList)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addMoods(receivedData : MoodDetailAllModel){
        val cv = ContentValues()
        cv.put(COLUMN_MOOD_POSI,receivedData.moodPosition)
        cv.put(COLUMN_DATE,receivedData.date)
        cv.put(COLUMN_MOOD_TWO,receivedData.moodTwo)
        cv.put(COLUMN_TIME,receivedData.time)
        cv.put(COLUMN_NOTE,receivedData.note)
        val db = this.writableDatabase
        db.insert(TABLE_NAME,null,cv)
        db.close()
    }

    fun getMoodDetail(date : String) : Cursor{
        val db = this.readableDatabase
        val result = db.rawQuery("Select * from $TABLE_NAME Where $COLUMN_DATE = ?", arrayOf<String>(date))
        return result
    }

    fun getMoodDetailsAll() : Cursor{
        val db = this.readableDatabase
        val result = db.rawQuery("Select * from $TABLE_NAME",null)
        return result
    }

    fun deleteData(date : String,time : String){
        val db = this.writableDatabase
        db!!.delete(TABLE_NAME,"$COLUMN_DATE=? and $COLUMN_TIME=?", arrayOf(date,time))
    }

    fun addMoodList(name : String,image : Byte){

    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "moodtracker.db"
        private val TABLE_NAME = "moodtable"
        val COLUMN_DATE = "date"
        val COLUMN_TIME = "time"
        val COLUMN_MOOD_POSI = "mood_posi"
        val COLUMN_MOOD_TWO = "mood_two"
        val COLUMN_NOTE = "note"

        private val TABLE_MOOD_LIST = "moodlist"
        val MOOD_NAME = "moodname"
        val MOOD_IMAGE= "moodimage"

        private val TABLE_MOODTWO_LIST = "moodtwolist"
        val MOODTWO_NAME = "moodtwoname"
        val MOODTWO_IMAGE= "moodtwoimage"
    }
}