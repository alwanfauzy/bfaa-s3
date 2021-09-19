package com.example.githubdatabase.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite_user")
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,
    val login: String? = null,
    val type: String? = null,
    val avatar_url: String? = null
) : Parcelable
