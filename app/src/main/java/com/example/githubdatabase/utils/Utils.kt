package com.example.githubdatabase.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubdatabase.model.User

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions().override(300, 300))
        .centerCrop()
        .into(this)
}

fun List<User>.toArrayList(): ArrayList<User> {
    val listUser = ArrayList<User>()
    for (user in this) {
        val userMapped = User(
            user.id,
            user.login,
            user.type,
            user.avatar_url,
        )
        listUser.add(userMapped)
    }
    return listUser
}