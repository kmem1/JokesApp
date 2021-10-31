package com.example.jokesapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jokesapp.R
import com.example.jokesapp.common.LoadState
import com.example.jokesapp.databinding.ProgressItemBinding

class JokesLoadStateAdapter(private val retry: () -> Unit, private val errorMessage: String) :
    RecyclerView.Adapter<JokesLoadStateAdapter.ViewHolder>() {

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }

    var loadState: LoadState = LoadState.Success
        set(loadState) {
            if (field != loadState) {
                val displayOldItem = displayLoadStateAsItem(field)
                val displayNewItem = displayLoadStateAsItem(loadState)

                if (displayOldItem && !displayNewItem) {
                    recyclerView?.post { notifyItemRemoved(0) }
                } else if (displayNewItem && !displayOldItem) {
                    recyclerView?.post { notifyItemInserted(0) }
                } else if (displayOldItem && displayNewItem) {
                    recyclerView?.post { notifyItemChanged(0) }
                }

                field = loadState
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.progress_item, parent, false)
        val binding = ProgressItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(loadState)
    }

    override fun getItemCount(): Int = if (displayLoadStateAsItem(loadState)) 1 else 0

    inner class ViewHolder(private val binding: ProgressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Failed) {
                binding.retryButton.setOnClickListener { retry() }
                binding.errorMsg.text = errorMessage
            }

            binding.progressBar.visibility = toVisibility(loadState is LoadState.Loading)
            binding.errorMsg.visibility = toVisibility(loadState is LoadState.Failed)
            binding.retryButton.visibility = toVisibility(loadState is LoadState.Failed)
        }

        private fun toVisibility(constraint: Boolean): Int = if (constraint) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading || loadState is LoadState.Failed
    }
}