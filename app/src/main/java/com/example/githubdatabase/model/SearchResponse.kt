package com.example.githubdatabase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponse(
    val items: ArrayList<User>,
) : Parcelable