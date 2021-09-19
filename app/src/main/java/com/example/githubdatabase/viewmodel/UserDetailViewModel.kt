package com.example.githubdatabase.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubdatabase.model.User
import com.example.githubdatabase.model.UserDetail
import com.example.githubdatabase.repository.local.FavoriteUserDao
import com.example.githubdatabase.repository.local.UserDatabase
import com.example.githubdatabase.repository.remote.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val userDetail = MutableLiveData<UserDetail>()
    val isSuccess = MutableLiveData<Boolean>()
    private var userDao: FavoriteUserDao? = null
    private var userDB: UserDatabase? = UserDatabase.getDatabase(application)

    init {
        userDao = userDB?.favoriteUserDao()
    }

    fun setDetailUser(username: String) {
        RetrofitClient.apiInstance
            .getDetailUsers(username)
            .enqueue(object : Callback<UserDetail> {
                override fun onResponse(
                    call: Call<UserDetail>,
                    response: Response<UserDetail>,
                ) {
                    if (response.isSuccessful) {
                        userDetail.postValue(response.body())
                        isSuccess.postValue(true)
                    }
                }

                override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                    Log.e("Failure", "${t.message}")
                    isSuccess.postValue(false)
                }
            })
    }

    fun getDetailUser(): LiveData<UserDetail> {
        return userDetail
    }

    fun addToFavorite(data: User) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = User(
                data.id,
                data.login,
                data.type,
                data.avatar_url,
            )
            userDao?.addToFavorite(user)
        }
    }

    fun removeFavoriteUser(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeUserFavorites(id)
        }
    }

    fun isFavoriteUser(id: Int) = userDao?.isFavoriteUser(id)
}