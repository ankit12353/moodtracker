package com.android.mood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.mood.R
import com.android.mood.model.NewsModel

class NewsAdapter(private val newsClicked: NewsClicked) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val newsList : ArrayList<NewsModel> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_news,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList[position])
        holder.itemView.setOnClickListener{
            newsClicked.onNewsClicked(holder.adapterPosition)
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        private val title : TextView = view.findViewById(R.id.tvNews)
        private val image : ImageView = view.findViewById(R.id.ivNews)

        fun bind(news : NewsModel){
            title.text = news.title

        }
    }

    fun updateList(newsList : ArrayList<NewsModel>){
        newsList.clear()
        newsList.addAll(newsList)
        notifyDataSetChanged()
    }

    interface NewsClicked{
        fun onNewsClicked(position: Int)
    }
}