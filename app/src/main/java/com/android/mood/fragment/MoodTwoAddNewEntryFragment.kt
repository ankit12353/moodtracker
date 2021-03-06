package com.android.mood.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.helper.DataBaseHelper
import com.android.mood.activity.MoodActivity
import com.android.mood.activity.NoteActivity
import com.android.mood.adapter.MoodAdapter
import com.android.mood.helper.Constant.DATE
import com.android.mood.helper.Constant.MOODOBJECT_MOOD_MOODTWO
import com.android.mood.helper.Constant.MOODPOSITION_MOOD_MOODTWO
import com.android.mood.helper.Constant.TIME_MOOD_MOODTWO
import com.android.mood.model.MoodBitmapModel
import com.android.mood.model.AllEntryDetailModel

class MoodTwoAddNewEntryFragment : Fragment(),MoodAdapter.MoodSelected {

    private var mContext: Context? = null
    private var v: View? = null
    private var date: String? = null
    private var time: String? = null
    private var moodPosition: String? = null
    private var rvMoodTwo: RecyclerView? = null
    private var mood: MoodBitmapModel? = null
    private var allEntryObject: AllEntryDetailModel? = null
    private var moodTwoList : ArrayList<MoodBitmapModel> = ArrayList()
    private var dbHandler : DataBaseHelper?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_mood_two_add_new_entry, container, false)

        init()
        v!!.findViewById<TextView>(R.id.tvSelectedMood).text = mood!!.moodName
        v!!.findViewById<ImageView>(R.id.ivSelectedMood).setImageBitmap(mood!!.moodImage)

        rvMoodTwo!!.layoutManager = GridLayoutManager(mContext, 5)
        rvMoodTwo!!.setHasFixedSize(true)
        rvMoodTwo!!.adapter = MoodAdapter(mContext!!, moodTwoList, this)

        v!!.findViewById<LinearLayout>(R.id.forward_btn_mood_two).setOnClickListener { openAllMoodTwoListFragment() }

        return v
    }

    private fun init() {
        rvMoodTwo = v!!.findViewById<RecyclerView>(R.id.rv_mood_two)
        dbHandler = DataBaseHelper(mContext!!,null)
        date = arguments?.getString(DATE)
        time = arguments?.getString(TIME_MOOD_MOODTWO)
        moodPosition = arguments?.getString(MOODPOSITION_MOOD_MOODTWO)
        mood = arguments?.getParcelable(MOODOBJECT_MOOD_MOODTWO)
        moodTwoList = dbHandler!!.getMoodTwoList()
    }

    private fun openAllMoodTwoListFragment(){

    }

    fun getSelectedMoodTwo() {
        val etNote = view!!.findViewById<EditText>(R.id.et_note)
        val note = etNote.text.toString().trim()
        val moodTwoText = StringBuilder("")
        for (moodTwoModel: MoodBitmapModel in moodTwoList) {
            if (moodTwoModel.isSelected()) {
                if (!moodTwoText.isEmpty()) {
                    moodTwoText.append(",")
                }
                moodTwoText.append(moodTwoModel.moodName)
            }
        }
        //saving data to database
        allEntryObject = AllEntryDetailModel(date!!, moodPosition!!, moodTwoText.toString(), time!!, note)
        dbHandler!!.addEntry(allEntryObject!!)
        val intent = Intent(mContext,NoteActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(DATE,allEntryObject!!.date)
        startActivity(intent)
        (activity as MoodActivity).finishActivity()
    }

    override fun onMoodSelected(position: Int) {    }
}