package com.android.mood.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.BitmapFactory
import com.android.mood.model.AllEntryDetailModel
import com.android.mood.model.MoodBitmapModel
import java.text.SimpleDateFormat


class DataBaseHelper(val mContext : Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(mContext ,DATABASE_NAME,factory,DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableMood = "CREATE TABLE $TABLE_ENTRY_LIST ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COLUMN_DATE TEXT ,$COLUMN_TIME TEXT,$COLUMN_MOOD_POSI TEXT,$COLUMN_MOODTWO_POSI TEXT,$COLUMN_NOTE TEXT)"
        val tableMoodList = "CREATE TABLE $TABLE_MOOD_LIST($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COLUMN_MOOD_NAME TEXT ,$COLUMN_MOOD_IMAGE BLOB)"
        val tableMoodTwoList = "CREATE TABLE $TABLE_MOODTWO_LIST($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COLUMN_MOOD_NAME TEXT ,$COLUMN_MOOD_IMAGE BLOB)"
        db!!.execSQL(createTableMood)
        db.execSQL(tableMoodList)
        db.execSQL(tableMoodTwoList)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_ENTRY_LIST")
        onCreate(db)
    }

    fun addEntry(receivedData : AllEntryDetailModel){
        val cv = ContentValues()
        cv.put(COLUMN_MOOD_POSI,receivedData.moodPosition)
        cv.put(COLUMN_DATE,receivedData.date)
        cv.put(COLUMN_MOODTWO_POSI,receivedData.moodTwo)
        cv.put(COLUMN_TIME,receivedData.time)
        cv.put(COLUMN_NOTE,receivedData.note)
        val db = this.writableDatabase
        db.insert(TABLE_ENTRY_LIST,null,cv)
        db.close()
    }
    fun addMood(name: String, image: ByteArray) {
        val cv = ContentValues()
        cv.put(COLUMN_MOOD_NAME, name)
        cv.put(COLUMN_MOOD_IMAGE, image)
        val db = this.writableDatabase
        db.insert(TABLE_MOOD_LIST, null, cv)
        db.close()
    }
    fun addMoodTwo(name : String,image : ByteArray){
        val cv = ContentValues()
        cv.put(COLUMN_MOOD_NAME,name)
        cv.put(COLUMN_MOOD_IMAGE,image)
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
                val id = result.getInt(result.getColumnIndex(COLUMN_ID))
                val name=result.getString(result.getColumnIndex(COLUMN_MOOD_NAME))
                val image = result.getBlob(result.getColumnIndex(COLUMN_MOOD_IMAGE))
                val b1 = BitmapFactory.decodeByteArray(image, 0, image!!.size)
                val mood = MoodBitmapModel(name,b1)
                mood.setId(id)
                moodList.add(mood)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return moodList
    }
    fun getMoodTwoList() : ArrayList<MoodBitmapModel>{
        val moodList :ArrayList<MoodBitmapModel> = ArrayList()
        val db = this.readableDatabase
        val result = db.rawQuery("Select * from $TABLE_MOODTWO_LIST",null)
        if(result.moveToFirst()){
            do {
                val name=result.getString(result.getColumnIndex(COLUMN_MOOD_NAME))
                val image = result.getBlob(result.getColumnIndex(COLUMN_MOOD_IMAGE))
                val b1 = BitmapFactory.decodeByteArray(image, 0, image!!.size)
                moodList.add(MoodBitmapModel(name,b1))
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return moodList
    }
    fun getEntryDetail(date : String) : ArrayList<AllEntryDetailModel>{
        val allEntryList : ArrayList<AllEntryDetailModel> = ArrayList()
        val db = this.readableDatabase
        val result =  db.rawQuery("Select * from $TABLE_ENTRY_LIST Where $COLUMN_DATE = ?", arrayOf(date))
        if (result.moveToFirst()) {
            do {
                val id = result.getInt(result.getColumnIndex(COLUMN_ID))
                val moodPosition = result.getString(result.getColumnIndex(COLUMN_MOOD_POSI))
                val moodTwo = result.getString(result.getColumnIndex(COLUMN_MOODTWO_POSI))
                val time = result.getString(result.getColumnIndex(COLUMN_TIME))
                val note = result.getString(result.getColumnIndex(COLUMN_NOTE))
                val mood = AllEntryDetailModel(date,moodPosition,moodTwo,time,note)
                mood.setId(id)
                allEntryList.add(mood)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        allEntryList.sortWith(Comparator { t1, t2 -> SimpleDateFormat("hh:mm a").parse(t1.time).compareTo(SimpleDateFormat("hh:mm a").parse(t2.time))})
        return allEntryList
    }
    fun getAllEntries() : ArrayList<AllEntryDetailModel>{
        val allEntriesList : ArrayList<AllEntryDetailModel> = ArrayList()
        val db = this.readableDatabase
        val result =  db.rawQuery("Select * from $TABLE_ENTRY_LIST",null)
        if(result.moveToFirst()){
            do {
                val id = result.getInt(result.getColumnIndex(COLUMN_ID))
                val date = result.getString(result.getColumnIndex(COLUMN_DATE))
                val moodPosition = result.getString(result.getColumnIndex(COLUMN_MOOD_POSI))
                val moodTwo = result.getString(result.getColumnIndex(COLUMN_MOODTWO_POSI))
                val time = result.getString(result.getColumnIndex(COLUMN_TIME))
                val note = result.getString(result.getColumnIndex(COLUMN_NOTE))
                val mood = AllEntryDetailModel(date,moodPosition,moodTwo,time,note)
                mood.setId(id)
                allEntriesList.add(mood)
            } while (result.moveToNext())
        }
        allEntriesList.sortWith(Comparator { o1, o2 -> SimpleDateFormat("dd/MM/yyyyhh:mm a").parse(o1.date+o1.time).compareTo(SimpleDateFormat("dd/MM/yyyyhh:mm a").parse(o2.date+o2.time)) })
        result.close()
        db.close()
        return allEntriesList
    }

    fun deleteEntry(id :Int){
        val db = this.writableDatabase
        db.delete(TABLE_ENTRY_LIST,"$COLUMN_ID=?", arrayOf(java.lang.String.valueOf(id)))
        db.close()
    }
    fun deleteMood(name : String){
        val db = this.writableDatabase
        db.delete(TABLE_MOOD_LIST,"$COLUMN_MOOD_NAME=?", arrayOf(name))
        db.close()
    }

    fun updateMood(id : Int,name : String,image : ByteArray){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_MOOD_NAME,name)
        cv.put(COLUMN_MOOD_IMAGE,image)
        db.update(TABLE_MOOD_LIST,cv,"$COLUMN_ID=?", arrayOf(java.lang.String.valueOf(id)))
        db.close()
    }
    fun updateMoodName(id : Int,name : String){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_MOOD_NAME,name)
        db.update(TABLE_MOOD_LIST,cv,"$COLUMN_ID=?", arrayOf(java.lang.String.valueOf(id)))
        db.close()
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "moodtracker.db"
        private const val TABLE_ENTRY_LIST = "moodtable"
        private const val TABLE_MOOD_LIST = "moodlist"
        private const val TABLE_MOODTWO_LIST = "moodtwolist"

        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_MOOD_POSI = "mood_posi"
        private const val COLUMN_MOODTWO_POSI = "mood_two"
        private const val COLUMN_NOTE = "note"
        private const val COLUMN_MOOD_NAME = "moodname"
        private const val COLUMN_MOOD_IMAGE= "moodimage"
        private const val COLUMN_ID= "s_no"

    }
}