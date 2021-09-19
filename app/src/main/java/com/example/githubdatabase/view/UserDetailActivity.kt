package com.example.githubdatabase.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.githubdatabase.R
import com.example.githubdatabase.adapter.SectionsPagerAdapter
import com.example.githubdatabase.databinding.ActivityUserDetailBinding
import com.example.githubdatabase.model.User
import com.example.githubdatabase.utils.loadImage
import com.example.githubdatabase.viewmodel.UserDetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserDetailActivity : AppCompatActivity() {
    private var _binding: ActivityUserDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: UserDetailViewModel
    private lateinit var extraUser: User
    private var statusFavorite: Boolean = false

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.app_follower,
            R.string.app_following
        )
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSectionPager()

        extraUser = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val login = extraUser.login
        setupDetailViewModel(login!!)
        setStatusFavorite()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.user_detail_menu, menu)

        val item: MenuItem = menu!!.findItem(R.id.favorite_menu)
        if (statusFavorite) {
            item.setIcon(R.drawable.ic_favorite)
        } else {
            item.setIcon(R.drawable.ic_favorite_border)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_menu -> {
                if (statusFavorite) {
                    item.setIcon(R.drawable.ic_favorite_border)
                    statusFavorite = false
                    detailViewModel.removeFavoriteUser(extraUser.id!!)
                    Snackbar.make(
                        binding.root, R.string.remove_favorite,
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    item.setIcon(R.drawable.ic_favorite)
                    statusFavorite = true
                    detailViewModel.addToFavorite(extraUser)
                    Snackbar.make(
                        binding.root,
                        R.string.add_favorite,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                true
            }
            else -> true
        }
    }

    private fun setupDetailViewModel(getUsername: String) {
        showLoading(true)
        detailViewModel = ViewModelProvider(this).get(UserDetailViewModel::class.java)
        detailViewModel.setDetailUser(getUsername)
        detailViewModel.getDetailUser().observe(this, {

            binding.tvDetailUsername.text = it.login ?: "-"
            binding.tvDetailName.text = it.name ?: "-"
            binding.imgDetailAvatar.loadImage(it.avatar_url)
            binding.tvDetailCompany.text = it.company ?: "-"
            binding.tvDetailLocation.text = it.location ?: "-"
            binding.tvDetailRepository.text = it.public_repos ?: "-"
            binding.tvDetailFollower.text = resources.getString(R.string.followers, it.followers)
            binding.tvDetailFollowing.text = resources.getString(R.string.following, it.following)

            showLoading(false)
        })
    }

    private fun setupSectionPager() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setStatusFavorite() {
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.isFavoriteUser(extraUser.id!!)
            statusFavorite = count!! > 0
        }
    }
}