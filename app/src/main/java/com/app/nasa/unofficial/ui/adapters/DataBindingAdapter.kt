package com.app.nasa.unofficial.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.nasa.unofficial.BR
import com.app.nasa.unofficial.utils.OnRecyclerViewItemClick

abstract class DataBindingAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    clickListener: OnRecyclerViewItemClick
) :
    ListAdapter<T, DataBindingAdapter.DataBindingViewHolder<T>>(diffCallback) {
    private val onItemClick = clickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) =
        holder.bind(getItem(position))

    class DataBindingViewHolder<T>(
        private val binding: ViewDataBinding,
        private val clickListener: OnRecyclerViewItemClick
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

        fun bind(item: T) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }
    }
}
