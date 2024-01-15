package com.app.lillup.auth.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.lillup.R

class LoginAdapter : RecyclerView.Adapter<LoginAdapter.MyViewHolder>() {
    inner  class MyViewHolder(itemView: View) :ViewHolder(itemView) {
        val ivImage:ImageView=itemView.findViewById(R.id.ivImge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.login_desc_item_layout, parent, false)
        return MyViewHolder(inflate);
    }

    override fun getItemCount(): Int {
        return  3;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mAngleRotate = ("90f").toFloat()
        holder.ivImage.rotation=mAngleRotate

    }
}