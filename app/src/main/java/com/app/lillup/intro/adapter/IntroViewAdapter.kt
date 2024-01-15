package com.app.lillup.intro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.lillup.R
import com.bumptech.glide.Glide

class IntroViewAdapter(private val images: List<Int>,private val headers:List<String>,private val footers:List<String>,) :
    RecyclerView.Adapter<IntroViewAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle:TextView=itemView.findViewById(R.id.tvTitle)
        val tvHeader:TextView=itemView.findViewById(R.id.tvHeader)
        val tvSubTitle:TextView=itemView.findViewById(R.id.tvSubTitle)
        val tvFooter:TextView=itemView.findViewById(R.id.tvFooter)
        val ivImage:ImageView=itemView.findViewById(R.id.ivImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            LayoutInflater.from(parent.context).inflate(R.layout.intro_item_layout, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if(position==0){
            holder.tvHeader.visibility=View.VISIBLE
            holder.tvTitle.visibility=View.VISIBLE
            holder.tvSubTitle.visibility=View.VISIBLE
        }else{
            holder.tvTitle.visibility=View.GONE
            holder.tvHeader.visibility=View.GONE
            holder.tvSubTitle.visibility=View.VISIBLE
        }
        holder.tvSubTitle.text="${headers[position]}";
        holder.tvFooter.text="${footers[position]}";
        holder.ivImage.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }
}
