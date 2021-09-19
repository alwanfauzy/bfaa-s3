package com.example.githubdatabase.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubdatabase.adapter.UserAdapter
import com.example.githubdatabase.databinding.FragmentFollowingBinding
import com.example.githubdatabase.model.User
import com.example.githubdatabase.viewmodel.FollowViewModel

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private var username: String? = null
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        username = arguments?.getString("login")
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val extraUser =
            activity?.intent?.getParcelableExtra<User>(UserDetailActivity.EXTRA_USER) as User

        setupFollowingViewModel(extraUser.login!!)

        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupFollowingViewModel(username: String) {
        binding.progressBar.visibility = View.VISIBLE
        val followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowViewModel::class.java)
        followingViewModel.setFollowing(username)
        followingViewModel.getFollow().observe(requireActivity(), {
            if (it != null) {
                adapter.setData(it)
                binding.notFound.visibility = View.GONE
            }
            if (it.isEmpty()) {
                binding.notFound.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.GONE
        })
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter()
        binding.followingRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.followingRecyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val userDetailIntent = Intent(requireActivity(), UserDetailActivity::class.java)
                userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(userDetailIntent)
            }
        })
    }
}