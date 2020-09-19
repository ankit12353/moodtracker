package com.android.mood.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.BitmapFactory
import android.view.View
import com.android.mood.model.MoodBitmapModel
import com.android.mood.model.MoodDetailAllModel
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class DataBaseHelper(val mContext : Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(mContext ,DATABASE_NAME,factory,DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableMood = "CREATE TABLE $TABLE_NAME ($COLUMN_DATE TEXT ,$COLUMN_TIME TEXT,$COLUMN_MOOD_POSI TEXT,$COLUMN_MOOD_TWO TEXT,$COLUMN_NOTE TEXT)"
        val tableMoodList = "CREATE TABLE $TABLE_MOOD_LIST($MOOD_NAME TEXT PRIMARY KEY ,$MOOD_IMAGE BLOB)"
        val tableMoodTwoList = "CREATE TABLE $TABLE_MOODTWO_LIST($MOODTWO_NAME TEXT PRIMARY KEY ,$MOODTWO_IMAGE BLOB)"
        db!!.execSQL(createTableMood)
        db.execSQL(tableMoodList)
        db.execSQL(tableMoodTwoList)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addEntry(receivedData : MoodDetailAllModel){
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
    fun addMood(name : String,image : ByteArray) : Boolean{
        val cv = ContentValues()
        cv.put(MOOD_NAME, name)
        cv.put(MOOD_IMAGE, image)
        val db = this.writableDatabase
        db.insert(TABLE_MOOD_LIST, null, cv)
        db.close()
        return true

    }
    fun addMoodTwo(name : String,image : ByteArray){
        val cv = ContentValues()
        cv.put(MOODTWO_NAME,name)
        cv.put(MOODTWO_IMAGE,image)
        val db = this.writableDatabase
        db.insert(TABLE_MOODTWO_LIST,null,cv)
        db.close()
    }

    fun getMoodList() : ArrayList<MoodBitmapModel>{
        val moodList :ArrayList<MoodBitmapModel> = ArrayList()
        val db = this.readableDatabase
        val result = db.rawQuery("Select * from $TABLE_MOOD_LIST",null)
        if(result.moveToFirst()){
            do {
                val name=result.getString(result.getColumnIndex(MOOD_NAME))
                val image = result.getBlob(result.getColumnIndex(MOOD_IMAGE))
                val b1 = BitmapFactory.decodeByteArray(image, 0, image!!.size)
                moodList.add(MoodBitmapModel(name,b1))
            } while (result.moveToNext())
        }
        result.close()
        return moodList
    }
    fun getMoodTwoList() : ArrayList<MoodBitmapModel>{
        val moodList :ArrayList<MoodBitmapModel> = ArrayList()
        val db = this.readableDatabase
        val result = db.rawQuery("Select * from $TABLE_MOODTWO_LIST",null)
        if(result.moveToFirst()){
            do {
                val name=result.getString(result.getColumnIndex(MOODTWO_NAME))
                val image = result.getBlob(result.getColumnIndex(MOODTWO_IMAGE))
                val b1 = BitmapFactory.decodeByteArray(image, 0, image!!.size)
                moodList.add(MoodBitmapModel(name,b1))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return moodList
    }
    fun getEntryDetail(date : String) : Cursor{
        val db = this.readableDatabase
        val result = db.rawQuery("Select * from $TABLE_NAME Where $COLUMN_DATE = ?", arrayOf<String>(date))
        return result
    }
    fun getAllEntries() : Cursor{
        val db = this.readableDatabase
        val result = db.rawQuery("Select * from $TABLE_NAME",null)
        return result
    }

    fun deleteEntry(date : String,time : String){
        val db = this.writableDatabase
        db!!.delete(TABLE_NAME,"$COLUMN_DATE=? and $COLUMN_TIME=?", arrayOf(date,time))
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "moodtracker.db"
        private const val TABLE_NAME = "moodtable"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
        const val COLUMN_MOOD_POSI = "mood_posi"
        const val COLUMN_MOOD_TWO = "mood_two"
        const val COLUMN_NOTE = "note"

        private const val TABLE_MOOD_LIST = "moodlist"
        const val MOOD_NAME = "moodname"
        const val MOOD_IMAGE= "moodimage"

        private const val TABLE_MOODTWO_LIST = "moodtwolist"
        const val MOODTWO_NAME = "moodtwoname"
        const val MOODTWO_IMAGE= "moodtwoimage"
    }
}