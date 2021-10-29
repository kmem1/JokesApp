package com.example.jokesapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jokesapp.R
import com.example.jokesapp.databinding.JokeCategoryBinding
import com.example.jokesapp.domain.model.JokeCategory

class JokesCategoryAdapter(
    private val jokesCategories: ArrayList<JokeCategory>,
    private val listener: Listener? = null
) :
    RecyclerView.Adapter<JokesCategoryAdapter.ViewHolder>() {

    interface Listener {
        fun onCategoryClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.joke_category, parent, false)
        val binding = JokeCategoryBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(jokesCategories[position], listener, position)
    }

    override fun getItemCount(): Int = jokesCategories.size

    inner class ViewHolder(private val binding: JokeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: JokeCategory, listener: Listener?, position: Int) {
            binding.categoryName.text = category.name

            binding.root.setOnClickListener {
                listener?.onCategoryClick(position)
            }
        }
    }
}