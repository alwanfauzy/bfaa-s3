package com.example.githubdatabase.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubdatabase.adapter.UserAdapter
import com.example.githubdatabase.databinding.ActivityUserFavoriteBinding
import com.example.githubdatabase.model.User
import com.example.githubdatabase.utils.toArrayList
import com.example.githubdatabase.viewmodel.MainViewModel

class UserFavoriteActivity : AppCompatActivity() {
    private var _binding: ActivityUserFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)
        setupRecyclerView()
        setupMainViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupMainViewModel() {
        mainViewModel = ViewModelProvider(this).get(
            MainViewModel::class.java)

        mainViewModel.getFavoriteUsers()!!.observe(this) { userItems ->
            adapter.setData(userItems.toArrayList())
            if (userItems.count() != 0) {
                showEmpty(false)
            }else{
                showEmpty(true)
            }
            showLoading(false)
        }
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter()
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val userDetailIntent = Intent(this@UserFavoriteActivity, UserDetailActivity::class.java)
                userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(userDetailIntent)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showEmpty(state: Boolean) {
        if (state) {
            binding.imgEmpty.visibility = View.VISIBLE
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            binding.imgEmpty.visibility = View.GONE
            binding.tvEmpty.visibility = View.GONE
        }
    }
}