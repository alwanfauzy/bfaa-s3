package com.example.githubdatabase.repository.remote

import com.example.githubdatabase.BuildConfig
import com.example.githubdatabase.model.User
import com.example.githubdatabase.model.UserDetail
import com.example.githubdatabase.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {
    @GET("search/users")
    @Headers("Authorization: ${BuildConfig.GITHUB_TOKEN}")
    fun getSearchUsers(
        @Query("q") query: String,
    ): Call<SearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: ${BuildConfig.GITHUB_TOKEN}")
    fun getDetailUsers(
        @Path("username") username: String,
    ): Call<UserDetail>

    @GET("users/{username}/following")
    @Headers("Authorization: ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowingUsers(
        @Path("username") username: String,
    ): Call<ArrayList<User>>

    @GET("users/{username}/followers")
    @Headers("Authorization: ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowersUsers(
        @Path("username") username: String,
    ): Call<ArrayList<User>>
}