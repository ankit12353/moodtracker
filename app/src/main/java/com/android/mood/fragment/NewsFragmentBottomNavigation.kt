package com.android.mood.fragment

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.adapter.NewsAdapter
import com.android.mood.api.MySingleton
import com.android.mood.model.NewsModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class NewsFragmentBottomNavigation : Fragment() ,NewsAdapter.NewsClicked{

    private var mContext : Context ?= null
    private var rvNews : RecyclerView?= null
    private var v : View ?= null
    private lateinit var mAdapter: NewsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext= context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_news_navigationbar, container, false)

        init()
        fetchNews()
        rvNews!!.layoutManager = LinearLayoutManager(mContext!!)
        mAdapter = NewsAdapter(this)
        rvNews!!.adapter = mAdapter

        return v
    }

    private fun fetchNews(){
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->

            },
            Response.ErrorListener { error ->

            }
        )
        MySingleton.getInstance(mContext!!).addToRequestQueue(jsonObjectRequest)
    }

    private fun init(){
        rvNews= v!!.findViewById(R.id.rv_news)
    }

    override fun onNewsClicked(position: Int) {
        TODO("Not yet implemented")
    }
}