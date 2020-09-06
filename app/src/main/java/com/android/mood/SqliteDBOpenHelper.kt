package com.android.mood

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.android.mood.model.MoodDetailAllModel


class SqliteDBOpenHelper(mContext : Context ,factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(mContext ,DATABASE_NAME,factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_DATE + " TEXT ," + COLUMN_TIME +" TEXT," + COLUMN_MOOD_POSI + " TEXT," + COLUMN_MOOD_TWO + " TEXT," + COLUMN_NOTE + " TEXT)"
        db!!.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addMoods(recivedData : MoodDetailAllModel){
        val cv = ContentValues()
        cv.put(COLUMN_MOOD_POSI,recivedData.moodPosition)
        cv.put(COLUMN_DATE,recivedData.date)
        cv.put(COLUMN_MOOD_TWO,recivedData.moodTwo)
        cv.put(COLUMN_TIME,recivedData.time)
        cv.put(COLUMN_NOTE,recivedData.note)
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

    fun updateData(date : String,time : String){

    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "moodtracker.db"
        val TABLE_NAME = "moodtable"
        val COLUMN_DATE = "date"
        val COLUMN_TIME = "time"
        val COLUMN_MOOD_POSI = "mood_posi"
        val COLUMN_MOOD_TWO = "mood_two"
        val COLUMN_NOTE = "note"
    }
}