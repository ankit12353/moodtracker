package com.android.mood.fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.SqliteDBOpenHelper
import com.android.mood.activity.NoteActivity
import com.android.mood.adapter.MoodTwoAdapter
import com.android.mood.constants.Constant.*
import com.android.mood.model.MoodDetailAllModel
import com.android.mood.model.MoodModel

class MoodTwoAddNewEntryFragment : Fragment() {

    private var mContext: Context? = null
    private var v: View? = null
    private var date: String? = null
    private var time: String? = null
    private var moodPosition: String? = null
    private var tvSelectedMood: TextView? = null
    private var ivSelectedMood: ImageView? = null
    private var rvMoodTwo: RecyclerView? = null
    private var ivForwardBtn: ImageView? = null
    private var data: MoodDetailAllModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private val moodTwo = arrayListOf<MoodModel>(
        MoodModel("Cycling", R.drawable.cycle),
        MoodModel("Reading", R.drawable.book),
        MoodModel("Cleaning", R.drawable.clean),
        MoodModel("Family", R.drawable.family),
        MoodModel("Friend", R.drawable.friend),
        MoodModel("Fruits", R.drawable.fruit),
        MoodModel("Gym", R.drawable.gym),
        MoodModel("Gaming", R.drawable.game),
        MoodModel("Movies", R.drawable.monitor),
        MoodModel("Jogging", R.drawable.run),
        MoodModel("Shopping", R.drawable.shopping),
        MoodModel("Yoga", R.drawable.yoga)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_mood_two_add_new_entry, container, false)

        init()
        val moodAddNewEntryFragment = MoodAddNewEntryFragment()
        val moodDetail: MoodModel =
            moodAddNewEntryFragment.getMoodDataOfPosition(moodPosition!!.toInt())
        tvSelectedMood!!.text = moodDetail.moodName
        ivSelectedMood!!.setImageResource(moodDetail.moodImage)

        rvMoodTwo!!.layoutManager = GridLayoutManager(mContext, 5)
        rvMoodTwo!!.setHasFixedSize(true)
        rvMoodTwo!!.adapter = MoodTwoAdapter(mContext!!, moodTwo, ivForwardBtn!!)

        ivForwardBtn!!.setOnClickListener(View.OnClickListener {
            getSelectedMoodTwo()
        })
        return v
    }

    private fun init() {
        tvSelectedMood = v!!.findViewById<TextView>(R.id.tvSelectedMood)
        ivSelectedMood = v!!.findViewById<ImageView>(R.id.ivSelectedMood)
        rvMoodTwo = v!!.findViewById<RecyclerView>(R.id.rv_mood_two)
        ivForwardBtn = v!!.findViewById<ImageView>(R.id.iv_forward_btn_mood_two)

        date = arguments?.getString(DATE_MOOD_MOODTWO)
        time = arguments?.getString(TIME_MOOD_MOODTWO)
        moodPosition = arguments?.getString(MOODPOSITION_MOOD_MOODTWO)
    }

    private fun getSelectedMoodTwo() {
        val etNote = view!!.findViewById<EditText>(R.id.et_note)
        val note = etNote.text.toString().trim()
        val moodTwoText = StringBuilder("")
        for (moodTwoModel: MoodModel in moodTwo) {
            if (moodTwoModel.isSelected()) {
                if (!moodTwoText.isEmpty()) {
                    moodTwoText.append(",")
                }
                moodTwoText.append(moodTwoModel.moodName)
            }
        }
        //saving data to database
        val dbHandler = SqliteDBOpenHelper(mContext!!, null)
        data = MoodDetailAllModel(date!!, moodPosition!!, moodTwoText.toString(), time!!, note)
        dbHandler.addMoods(data!!)
        (activity as NoteActivity).finishActivity()
    }
}