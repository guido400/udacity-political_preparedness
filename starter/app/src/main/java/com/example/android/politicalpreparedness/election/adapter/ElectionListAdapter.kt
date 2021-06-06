package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionListItemBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    //TODO: Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    //TODO: Create ElectionViewHolder
    class ElectionViewHolder(binding: ElectionListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Election) {
            val res = itemView.context.resources
        }
        //TODO: Add companion object to inflate ViewHolder (from)
        companion object {
            fun from(parent: ViewGroup): ElectionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ElectionListItemBinding.inflate(layoutInflater, parent, false)
                return ElectionViewHolder(binding)
            }
        }

    }

    //TODO: Create ElectionDiffCallback
    class ElectionDiffCallback :
        DiffUtil.ItemCallback<Election>() {
        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem == newItem
        }
    }
}


//TODO: Create ElectionListener

class ElectionListener(val clickListener: (id:Int) -> Unit) {
    fun onClick(election:Election) = clickListener(election.id)
}