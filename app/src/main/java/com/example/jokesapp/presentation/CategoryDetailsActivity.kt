package com.example.jokesapp.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jokesapp.JokesApplication
import com.example.jokesapp.R
import com.example.jokesapp.common.LoadState
import com.example.jokesapp.common.State
import com.example.jokesapp.databinding.ActivityCategoryDetailsBinding
import com.example.jokesapp.domain.model.Joke
import com.example.jokesapp.presentation.adapters.JokesAdapter
import com.example.jokesapp.presentation.adapters.JokesLoadStateAdapter
import com.example.jokesapp.presentation.scroll_listeners.PaginationScrollListener
import com.example.jokesapp.presentation.viewmodels.CategoryDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CategoryDetailsActivity : AppCompatActivity() {

    private var _binding: ActivityCategoryDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryDetailsViewModel by viewModels()

    private val jokes = ArrayList<Joke>()
    private var isLoading = false

    private val jokesAdapter = JokesAdapter(jokes)
    private var loadStateAdapter: JokesLoadStateAdapter? = null

    private var categoryName = ""
    private var categoryId = 0
    private var isRestored = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCategoryDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        categoryName = intent.extras?.getString(EXTRA_CATEGORY_NAME, "JokesApp") ?: ""
        categoryId = intent.extras?.getInt(EXTRA_CATEGORY_ID) ?: 0
        isRestored = intent.extras?.getBoolean(EXTRA_IS_RESTORED) ?: false

        loadStateAdapter =
            JokesLoadStateAdapter({ loadJokes() }, resources.getString(R.string.error_message))

        binding.recyclerView.adapter = ConcatAdapter(jokesAdapter, loadStateAdapter!!)
        binding.recyclerView.layoutManager = LinearLayoutManager(baseContext)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = categoryName

        if (savedInstanceState != null) {
            jokes.addAll(
                savedInstanceState.getParcelableArrayList<Joke>(SAVED_JOKES) as? ArrayList<Joke>
                    ?: arrayListOf()
            )
            jokesAdapter.notifyDataSetChanged()
        }

        if (jokes.isEmpty()) {
            loadJokes()
        }

        addScrollListenerToRecyclerView()
    }

    override fun onResume() {
        super.onResume()

        JokesApplication.setLastCategoryIdInPreferences(categoryId)
    }

    private fun addScrollListenerToRecyclerView() {
        val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager

        binding.recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLoading(): Boolean = isLoading

            override fun loadItems() {
                loadJokes()
            }
        })
    }

    private fun loadJokes() {
        lifecycleScope.launchWhenCreated {
            val jokesFlow =
                if (!isRestored) {
                    viewModel.getJokes(categoryId)
                } else {
                    viewModel.getJokesFromCache(categoryId)
                }

            isRestored = false

            jokesFlow.collect { result ->
                when (result) {
                    is State.Loading -> {
                        loadStateAdapter?.loadState = LoadState.Loading
                        isLoading = true
                    }
                    is State.Success -> {
                        jokes.addAll(result.data)
                        jokesAdapter.notifyDataSetChanged()
                        loadStateAdapter?.loadState = LoadState.Success
                        isLoading = false
                        saveState()
                        this@launchWhenCreated.cancel()
                    }
                    is State.Failed -> {
                        loadStateAdapter?.loadState = LoadState.Failed
                        this@launchWhenCreated.cancel()
                    }
                }
            }
        }
    }

    private fun saveState() {
        JokesApplication.setCachedCategoryIdInPreferences(categoryId)
        JokesApplication.setCategoryNameInPreferences(categoryName)
    }

    override fun onSupportNavigateUp(): Boolean {
        JokesApplication.setLastCategoryIdInPreferences(0)
        finish()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        JokesApplication.setLastCategoryIdInPreferences(0)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList(SAVED_JOKES, jokes)
    }

    companion object {
        const val EXTRA_CATEGORY_ID = "category_id"
        const val EXTRA_CATEGORY_NAME = "category_name"
        const val EXTRA_IS_RESTORED = "is_restored"
        private const val SAVED_JOKES = "saved_jokes"
    }
}