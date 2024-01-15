package com.app.lillup.utils

import android.content.Context
import android.widget.ImageView
import com.app.lillup.R
import com.bumptech.glide.Glide

object GlideHelper {
    fun showLocalImage(context:Context,imageView: ImageView,url:String):Unit{
        Glide
            .with(context)
            .load(url)
            .centerCrop()
            .placeholder(AppConst.DEFAULT_IMAGE)
            .into(imageView);
    }
}