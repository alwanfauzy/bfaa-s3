package com.example.githubdatabase.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubdatabase.model.SearchResponse
import com.example.githubdatabase.model.User
import com.example.githubdatabase.repository.local.FavoriteUserDao
import com.example.githubdatabase.repository.local.UserDatabase
import com.example.githubdatabase.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val listUsers = MutableLiveData<ArrayList<User>>()
    val isSuccess = MutableLiveData<Boolean>()
    private var userDao: FavoriteUserDao? = null
    private var userDB: UserDatabase? = UserDatabase.getDatabase(application)

    init {
        userDao = userDB?.favoriteUserDao()
    }

    fun setUsers(query: String) {
        RetrofitClient.apiInstance
            .getSearchUsers(query)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>,
                ) {
                    if (response.isSuccessful) {
                        isSuccess.postValue(true)
                        listUsers.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    Log.e("Failure", "${t.message}")
                    isSuccess.postValue(false)
                }
            })
    }

    fun getUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }

    fun getFavoriteUsers(): LiveData<List<User>>? {
        return userDao?.getFavoriteUser()
    }
}