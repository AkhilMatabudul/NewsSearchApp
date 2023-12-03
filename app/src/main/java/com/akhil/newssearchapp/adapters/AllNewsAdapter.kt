package com.akhil.newssearchapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.akhil.newssearchapp.activities.MainActivity
import com.akhil.newssearchapp.R
import com.akhil.newssearchapp.activities.WebviewActivity
import com.akhil.newssearchapp.modals.Article

import com.akhil.newssearchapp.utils.AppConstants


class AllNewsAdapter(newsObject: List<Article>, context: Context, binding: MainActivity) : RecyclerView.Adapter<AllNewsAdapter.NewsViewHolder>() {
    //variable declaration
    private val newsObjects: List<Article>
    private val binding: MainActivity
    private val context:Context

    //init constroctor to initialization
    init {
        this.newsObjects = newsObject
        this.binding = binding
        this.context = context
    }

    //view loader
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.latest_news_item, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NewsViewHolder, @SuppressLint("RecyclerView") position: Int) {

        //setting data to views
        holder.setData(newsObjects[position], holder.itemView.context)

        holder.speakBtn.setOnClickListener {
            if(newsObjects[position].description==null ||newsObjects[position].description=="null"){
                binding.speakText("Description is not available")
            }else {
                binding.speakText(newsObjects[position].description)
            }
        }
        holder.linkBtn.setOnClickListener {

            context.startActivity(Intent(context,WebviewActivity::class.java).putExtra("url",newsObjects[position].url)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemCount(): Int {
        return newsObjects.size
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var newsTitle: TextView
        var author:TextView
        var publishDate:TextView
        var newsImage:ImageView
        var speakBtn:TextView
        var linkBtn:TextView

        init {
            newsTitle = itemView.findViewById<TextView>(R.id.title)
            author = itemView.findViewById<TextView>(R.id.auther)
            publishDate = itemView.findViewById<TextView>(R.id.publishat)
            newsImage = itemView.findViewById<ImageView>(R.id.newsImage)
            speakBtn = itemView.findViewById<TextView>(R.id.play)
            linkBtn = itemView.findViewById<TextView>(R.id.view)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("ClickableViewAccessibility")
        fun setData(news: Article,context: Context) {

            newsTitle.text = Html.fromHtml("${news.title}")
            author.text = Html.fromHtml("${news.author}")
            publishDate.text = Html.fromHtml("${AppConstants.formatDate(news.publishedAt)}")

            Glide.with(itemView.context)
                .load(news.urlToImage)
                .placeholder(R.drawable.baseline_image_24)
                .error(R.drawable.baseline_broken_image_24)
                .into(newsImage)

        }

    }


}