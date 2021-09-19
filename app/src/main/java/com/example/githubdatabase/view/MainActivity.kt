package com.example.githubdatabase.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubdatabase.R
import com.example.githubdatabase.adapter.UserAdapter
import com.example.githubdatabase.databinding.ActivityMainBinding
import com.example.githubdatabase.model.User
import com.example.githubdatabase.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var searchView: SearchView
    private var extraSearch: String? = null


    companion object {
        private const val EXTRA_SEARCH = "EXTRA_SEARCH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            extraSearch = savedInstanceState.getString(EXTRA_SEARCH)
        }
        setupRecyclerView()
        setupMainViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showNotFound(state: Boolean) {
        if (state) {
            binding.notFound.visibility = View.VISIBLE
        } else {
            binding.notFound.visibility = View.GONE
        }
    }

    private fun statusNotFound(): Boolean {
        return binding.notFound.isVisible
    }

    private fun showStartSearch(state: Boolean) {
        if (state) {
            binding.imgStart.visibility = View.VISIBLE
            binding.tvStart.visibility = View.VISIBLE
        } else {
            binding.imgStart.visibility = View.GONE
            binding.tvStart.visibility = View.GONE
        }
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter()
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        binding.rvSearch.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val userDetailIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
                userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(userDetailIntent)
            }
        })
    }

    private fun setupMainViewModel() {
        showStartSearch(true)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.getUsers().observe(this, { userItems ->
            adapter.setData(userItems)
            if (userItems.isNotEmpty() || !statusNotFound()) {
                showStartSearch(false)
            }

            if (userItems.count() != 0) {
                showNotFound(false)
            } else {
                showNotFound(true)
            }
            showLoading(false)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.maxWidth = Integer.MAX_VALUE

        if (extraSearch != null && extraSearch != "") {
            searchView.run {
                onActionViewExpanded()
                requestFocusFromTouch()
                setQuery(extraSearch, false)
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showStartSearch(false)
                showNotFound(false)
                showLoading(true)
                mainViewModel.setUsers(query)
                closeKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorite -> {
                startActivity(Intent(this@MainActivity, UserFavoriteActivity::class.java))
                true
            }
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> true
        }
    }

    private fun closeKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (this::searchView.isInitialized) {
            extraSearch = searchView.query.toString().ifEmpty { null }
            outState.putString(EXTRA_SEARCH, extraSearch)
        }
        super.onSaveInstanceState(outState)
    }
}