package com.example.githubdatabase.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubdatabase.model.User
import com.example.githubdatabase.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val listFollow = MutableLiveData<ArrayList<User>>()
    val isSuccess = MutableLiveData<Boolean>()

    fun setFollowing(username: String) {
        RetrofitClient.apiInstance
            .getFollowingUsers(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>,
                ) {
                    if (response.isSuccessful) {
                        isSuccess.postValue(true)
                        listFollow.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.e("Failure", "${t.message}")
                }
            })
    }

    fun setFollowers(username: String) {
        RetrofitClient.apiInstance
            .getFollowersUsers(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>,
                ) {
                    if (response.isSuccessful) {
                        isSuccess.postValue(true)
                        listFollow.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.e("Failure", "${t.message}")
                }
            })
    }

    fun getFollow(): LiveData<ArrayList<User>> {
        return listFollow
    }
}