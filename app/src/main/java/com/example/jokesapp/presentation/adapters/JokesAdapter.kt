package com.example.jokesapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jokesapp.R
import com.example.jokesapp.databinding.JokeBinding
import com.example.jokesapp.domain.model.Joke

class JokesAdapter(private val jokes: ArrayList<Joke>) :
    RecyclerView.Adapter<JokesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.joke, parent, false)
        val binding = JokeBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(jokes[position])
    }

    override fun getItemCount(): Int = jokes.size

    inner class ViewHolder(private val binding: JokeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Joke) {
            binding.content.text = item.content
        }
    }
}