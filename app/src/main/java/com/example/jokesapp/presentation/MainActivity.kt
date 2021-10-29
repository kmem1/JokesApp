package com.example.jokesapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.jokesapp.JokesApplication
import com.example.jokesapp.data.remote.JokesApi
import com.example.jokesapp.databinding.ActivityMainBinding
import com.example.jokesapp.domain.model.JokeCategory
import com.example.jokesapp.domain.repository.JokesRepository
import com.example.jokesapp.presentation.adapters.JokesCategoryAdapter
import com.example.jokesapp.presentation.item_decorations.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), JokesCategoryAdapter.Listener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val categories = JokeCategory.getAllCategories()

    @Inject
    lateinit var api: JokesApi

    @Inject
    lateinit var repository: JokesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (JokesApplication.getLastCategoryIdFromPreferences() != 0) {
            openDetailsActivity()
        }

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.adapter = JokesCategoryAdapter(categories, this)
        binding.recyclerView.layoutManager = GridLayoutManager(baseContext, SPAN_COUNT)
        binding.recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                SPAN_COUNT,
                convertDpToPx(baseContext, RECYCLER_VIEW_MARGIN),
                true,
                0
            )
        )
    }

    override fun onResume() {
        super.onResume()

        JokesApplication.setLastCategoryIdInPreferences(0)
    }

    private fun convertDpToPx(context: Context, dp: Float): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    companion object {
        private const val SPAN_COUNT = 2
        private const val RECYCLER_VIEW_MARGIN = 16F
    }

    override fun onCategoryClick(position: Int) {
        val intent = Intent(this, CategoryDetailsActivity::class.java)
        intent.putExtra(CategoryDetailsActivity.EXTRA_CATEGORY_NAME, categories[position].name)
        intent.putExtra(CategoryDetailsActivity.EXTRA_CATEGORY_ID, categories[position].id)
        startActivity(intent)
    }

    private fun openDetailsActivity() {
        val id = JokesApplication.getLastCategoryIdFromPreferences()
        val name = JokesApplication.getCategoryNameFromPreferences()

        val intent = Intent(this, CategoryDetailsActivity::class.java)
        intent.putExtra(CategoryDetailsActivity.EXTRA_CATEGORY_NAME, name)
        intent.putExtra(CategoryDetailsActivity.EXTRA_CATEGORY_ID, id)
        intent.putExtra(CategoryDetailsActivity.EXTRA_IS_RESTORED, true)
        startActivity(intent)
    }
}