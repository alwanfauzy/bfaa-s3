package com.example.githubdatabase.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetail(
    val login: String? = null,
    val name: String? = null,
    val type: String? = null,
    val avatar_url: String? = null,
    val followers: Int? = null,
    val following: Int? = null,
    val company: String? = null,
    val location: String? = null,
    val public_repos: String? = null
) : Parcelable