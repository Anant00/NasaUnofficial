package com.app.nasa.unofficial.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.nasa.unofficial.BR
import com.app.nasa.unofficial.events.EventBus
import com.app.nasa.unofficial.events.PODFragmentClickEvents
import com.app.nasa.unofficial.utils.OnItemClick
import com.app.nasa.unofficial.utils.setLayoutHeight
import kotlinx.android.synthetic.main.item_recyclerview_main.view.*

abstract class DataBindingAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    clickListener: OnItemClick
) :
    ListAdapter<T, DataBindingAdapter.DataBindingViewHolder<T>>(diffCallback) {
    private val onItemClick = clickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)

        val viewHolder: DataBindingViewHolder<T> = DataBindingViewHolder(binding)
        binding.root.setOnClickListener {
            onItemClick.onItemClick(viewHolder.adapterPosition)
            EventBus.publish(PODFragmentClickEvents.OnRecyclerViewItemClick(viewHolder.adapterPosition))
        }

        binding.setVariable(BR.height, binding.root.image.setLayoutHeight())
        return viewHolder
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) =
        holder.bind(getItem(position))

    class DataBindingViewHolder<T>(
        private val binding: ViewDataBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }
    }
}
