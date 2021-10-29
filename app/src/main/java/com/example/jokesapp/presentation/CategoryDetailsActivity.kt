package com.example.jokesapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCategoryDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        loadStateAdapter =
            JokesLoadStateAdapter({ loadJokes() }, resources.getString(R.string.error_message))

        binding.recyclerView.adapter = ConcatAdapter(jokesAdapter, loadStateAdapter!!)
        binding.recyclerView.layoutManager = LinearLayoutManager(baseContext)

        loadJokes()
        addScrollListenerToRecyclerView()
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
            viewModel.getJokes(3).collect { result ->
                when (result) {
                    is State.Loading -> {
                        loadStateAdapter?.loadState = LoadState.Loading
                        isLoading = true
                    }
                    is State.Success -> {
                        Log.d("qwe", result.data.toString())
                        jokes.addAll(result.data)
                        jokesAdapter.notifyDataSetChanged()
                        loadStateAdapter?.loadState = LoadState.Success
                        isLoading = false
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
}